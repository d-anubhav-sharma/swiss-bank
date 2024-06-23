import { Button, List, ListItem, ListItemButton, ListItemText, TextField } from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";

const AccessManager = () => {
  const USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  const [allUsersAccessList, setAllUsersAccessList] = useState([]);
  const [allUsers, setAllUsers] = useState([]);
  const [activeUser, setActiveUser] = useState("");
  const [activeRole, setActiveRole] = useState("");
  const [roleToAdd, setRoleToAdd] = useState("");
  const [invalidRoleNameMessage, setInvalidRoleNameMessage] = useState("");
  const navBlue = "#2c3e50";

  const addRoleToUser = () => {
    if (!roleToAdd || !/^[A-Z_]{3,30}$/.test(roleToAdd)) {
      setInvalidRoleNameMessage("Invalid privilege name: " + roleToAdd);
      return;
    }
    setInvalidRoleNameMessage("");
    axios.post(USER_SERVICE_BASE_URL + "/admin/access/users/addRole/" + activeUser + "/" + roleToAdd).then(
      (role: any) => fetchAllUsers(),
      (error) => setInvalidRoleNameMessage(error.response.data.message || error.message)
    );
  };

  const fetchAllUsers = () => {
    axios.get(USER_SERVICE_BASE_URL + "/admin/access/users/all").then((userAccessReponse) => {
      setAllUsersAccessList(userAccessReponse.data);
      setAllUsers(userAccessReponse.data.map((user: any) => user.username));
    });
  };

  useEffect(() => {
    fetchAllUsers();
  }, []);

  return (
    <div>
      <h3>Access Manager</h3>
      <div style={{ display: "flex" }}>
        <div style={{ width: 400 }}>
          <h5>Users</h5>
          <List style={{ backgroundColor: navBlue }} disablePadding>
            {allUsers.map((user: string) => (
              <ListItem
                disablePadding
                key={user}
                style={{
                  backgroundColor: activeUser === user ? navBlue : "white",
                  color: activeUser === user ? "white" : "black",
                }}
              >
                <ListItemButton onClick={() => setActiveUser(user)}>
                  <input type="radio" checked={activeUser === user} />
                  <ListItemText>{user}</ListItemText>
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </div>
        <div>
          <h5>Roles</h5>
          <List style={{ backgroundColor: navBlue }} disablePadding>
            {allUsersAccessList
              .filter((userAccess: any) => userAccess.username === activeUser)
              .map((userAccess: any) => userAccess.roles)
              .flat()
              .map((role) => role.roleName)
              .map((roleName: string) => (
                <ListItem
                  disablePadding
                  key={roleName}
                  style={{
                    backgroundColor: activeRole === roleName ? navBlue : "white",
                    color: activeRole === roleName ? "white" : "black",
                  }}
                >
                  <ListItemButton onClick={() => setActiveRole(roleName)}>
                    <input type="radio" checked={activeRole === roleName} />
                    <ListItemText>{roleName}</ListItemText>
                  </ListItemButton>
                </ListItem>
              ))}
            <ListItem style={{ backgroundColor: "white" }}>
              <TextField
                size="small"
                variant="standard"
                placeholder="Role to add"
                value={roleToAdd}
                onChange={(event: any) => setRoleToAdd(event.target.value)}
              ></TextField>
              <Button
                onClick={() => addRoleToUser()}
                style={{ marginLeft: 50, width: 100, backgroundColor: "blue", maxWidth: 100, color: "white" }}
              >
                Add
              </Button>
            </ListItem>
            <ListItem style={{ backgroundColor: "white", color: "red" }}>
              <label>{invalidRoleNameMessage}</label>
            </ListItem>
          </List>
        </div>
      </div>
    </div>
  );
};
export default AccessManager;
