db = db.getSiblingDB('admin');

db.createUser({
  user: "swissbank",
  pwd: "swiss_pwd",
  roles: [
    { role: "root", db: "admin" },
    { role: "userAdminAnyDatabase", db: "admin" },
    { role: "dbAdminAnyDatabase", db: "admin" },
    { role: "readWriteAnyDatabase", db: "admin" }
  ]
});
