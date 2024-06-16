db = db.getSiblingDB("admin");

db.createUser({
  user: "swissbank",
  pwd: "swiss_pwd",
  roles: [
    { role: "root", db: "admin" },
    { role: "userAdminAnyDatabase", db: "admin" },
    { role: "dbAdminAnyDatabase", db: "admin" },
    { role: "readWriteAnyDatabase", db: "admin" },
  ],
});

db.collection("user").insertOne({
  _id: {
    $oid: "666080e310d3317ab0c0c8c6",
  },
  username: "anubhav_sharma",
  password: "$2a$10$r9LwHulRCDMMU2fHpyY29OKqFuuLjWmGPLE.6HvdzTLthF0sjS652",
  email: "anubhav.sharma@swiss-bank.com",
  roles: [
    {
      $oid: "6660810710d3317ab0c0c8c7",
    },
  ],
  _class: "com.swiss.bank.user.service.entities.User",
});
db.collection("role").insertOne({
  _id: {
    $oid: "6660810710d3317ab0c0c8c7",
  },
  username: "anubhav_sharma",
  roleName: "OWNER",
  privileges: ["STAFF", "ADMIN"],
  _class: "com.swiss.bank.user.service.entities.Role",
});
