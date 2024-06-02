SET DB_PATH=D:\\my_projects\\swiss-bank\\banking-services\\data
REM echo "========================================================================================"  > mongo_db.log 2>&1
REM echo "=========                    Starting mongo db server                       ============"  > mongo_db.log 2>&1
REM echo "========================================================================================"  > mongo_db.log 2>&1
cd %DB_PATH%
del *lock
mongod --dbpath=%DB_PATH% > mongo_db.log 2>&1
REM echo "========================================================================================"  > mongo_db.log 2>&1
REM echo "=========                    Stoping mongo db server                        ============"  > mongo_db.log 2>&1
REM echo "========================================================================================"  > mongo_db.log 2>&1
