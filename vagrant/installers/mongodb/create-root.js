db.getSiblingDB('admin').createUser(
  {
    user: "root",
    pwd: "root",
    roles: ["root"]
  });
