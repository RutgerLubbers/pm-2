db.getSiblingDB('pm').createUser(
  {
    user: "pm",
    pwd: "pm",
    roles: ["dbOwner"]
  });
