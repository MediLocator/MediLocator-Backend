import os
import time
import glob
import zipfile
import pandas as pd
import numpy as np
from selenium import webdriver
from selenium.webdriver.common.by import By
from sqlalchemy import create_engine, text

# 병원 정보 zip 파일 다운로드
options = webdriver.ChromeOptions()
download_dir = "/Users/hangyeol/MediLocator-Crawler/hospital"
os.makedirs(download_dir, exist_ok=True)

prefs = {
    "download.default_directory": download_dir,
    "download.prompt_for_download": False,
    "safebrowsing.enabled": True,
}
options.add_experimental_option("prefs", prefs)

driver = webdriver.Chrome(options=options)

try:
    driver.get("https://opendata.hira.or.kr/op/opc/selectOpenData.do?sno=11925")

    li_elements = driver.find_elements(By.CSS_SELECTOR, "dl.fileList li")
    last_file = li_elements[-1].find_element(By.TAG_NAME, "a")
    file_id = last_file.get_attribute("href").split("'")[1]

    driver.execute_script(f"fn_fileDown('{file_id}');")
    print("파일 다운로드를 시작합니다...")
    time.sleep(40)

    download_complete = False
    while not download_complete:
        time.sleep(1)
        downloaded_files = os.listdir(download_dir)
        if any(filename.endswith('.zip') for filename in downloaded_files):
            download_complete = True
            print("파일 다운로드가 완료되었습니다.")
except Exception as e:
    print(f"다운로드 중 오류 발생: {e}")
finally:
    driver.quit()

# 파일 압축 해제

zip_dir = download_dir
extract_dir = download_dir

zip_files = glob.glob(os.path.join(zip_dir, "*.zip"))

for zip_file_path in zip_files:
    print(f"압축 해제 중: {zip_file_path}")
    with zipfile.ZipFile(zip_file_path, 'r') as zip_ref:
        zip_ref.extractall(extract_dir)
        print(f"압축 해제 완료: {extract_dir}")

    os.remove(zip_file_path)
    print(f"원본 zip 파일 삭제 완료: {zip_file_path}")

print("모든 압축 해제 작업이 완료되었습니다.")

# xlsx 파일 데이터베이스에 저장

DATABASE_URL = "mysql+pymysql://user:password@localhost:3306/medilocator"
engine = create_engine(DATABASE_URL)

excel_dir = extract_dir

xlsx_files = glob.glob(os.path.join(excel_dir, "*.xlsx"))

columns = ["요양기관명", "종별코드명", "주소", "전화번호", "총의사수", "좌표(X)", "좌표(Y)"]

for xlsx_file in xlsx_files:
    print(f"처리 중인 파일: {xlsx_file}")

    data = pd.read_excel(xlsx_file)

    data.columns = data.columns.str.strip()
    data = data.rename(columns={
        "요양기관명": "name",
        "종별코드명": "category",
        "주소": "address",
        "전화번호": "phoneNumber",
        "총의사수": "total_doctors",
        "좌표(X)": "x_coordinate",
        "좌표(Y)": "y_coordinate",
    })

    for col in ["total_doctors", "x_coordinate", "y_coordinate"]:
        if col not in data.columns:
            data[col] = None

    for col in ["name", "category", "address", "phoneNumber"]:
        data[col] = data[col].where(pd.notnull(data[col]), None)

    data["total_doctors"] = pd.to_numeric(data["total_doctors"].fillna(0), downcast="integer")

    for col in ["x_coordinate", "y_coordinate"]:
        data[col] = data[col].astype(object)
        data[col] = data[col].where(pd.notnull(data[col]), None)

    filtered_data = data[["name", "category", "address", "phoneNumber", "total_doctors", "x_coordinate", "y_coordinate"]]

    with engine.connect() as conn:
        with conn.begin():
            for _, row in filtered_data.iterrows():
                query = text("""
                    INSERT INTO hospital (name, category, address, phone_number, total_doctors, x_coordinate, y_coordinate)
                    VALUES (:name, :category, :address, :phoneNumber, :totalDoctors, :xCoordinate, :yCoordinate)
                """)
                params = {
                    "name": row["name"],
                    "category": row["category"],
                    "address": row["address"],
                    "phoneNumber": row["phoneNumber"],
                    "totalDoctors": row["total_doctors"],
                    "xCoordinate": row["x_coordinate"],
                    "yCoordinate": row["y_coordinate"],
                }
                for key, value in params.items():
                    if isinstance(value, float) and np.isnan(value):
                        params[key] = None
                conn.execute(query, params)
    print(f"파일 처리 완료: {xlsx_file}")

print("모든 데이터 처리가 완료되었습니다.")