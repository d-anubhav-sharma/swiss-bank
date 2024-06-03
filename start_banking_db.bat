REM Deprecated in favour of adding docker
SET DB_PATH=D:\\my_projects\\swiss-bank\\banking-services\\data
cd %DB_PATH%
del *lock
mongod --dbpath=%DB_PATH% > mongo_db.log 2>&1
