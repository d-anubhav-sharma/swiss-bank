import { Button, List, ListItem, ListItemButton, ListItemText, TextField } from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";

const RoleManager = () => {
  const USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  const [allRolesPrivilegesList, setAllRolesPrivilegesList] = useState([]);
  const [activeRole, setActiveRole] = useState("ADMIN");
  const [activePrivilege, setActivePrivilege] = useState("ADMIN");
  const [allRoles, setAllRoles] = useState([]);
  const [newRoleName, setNewRoleName] = useState("");
  const [invalidNewRoleNameMessage, setInvalidNewRoleNameMessage] = useState("");
  const [invalidPrivilegeNameMessage, setInvalidPrivilegeNameMessage] = useState("");
  const [privilegeToAdd, setPrivilegeToAdd] = useState("");

  const navBlue = "#2c3e50";

  const addPrivilegeToRole = () => {
    if (!privilegeToAdd || !/^[A-Z_]{3,30}$/.test(privilegeToAdd)) {
      setInvalidPrivilegeNameMessage("Invalid privilege name: " + privilegeToAdd);
      return;
    }
    setInvalidPrivilegeNameMessage("");
    axios.post(USER_SERVICE_BASE_URL + "/admin/access/roles/addPrivilege/" + activeRole + "/" + privilegeToAdd).then(
      (role: any) => fetchAllRolesAndPrivileges(),
      (error) => setInvalidPrivilegeNameMessage(error.response.data.message || error.message)
    );
  };

  const createNewRole = () => {
    if (!newRoleName || !/^[A-Z_]{3,30}$/.test(newRoleName)) {
      setInvalidNewRoleNameMessage("Invalid new role name: " + newRoleName);
      return;
    }
    setInvalidNewRoleNameMessage("");
    axios
      .post(USER_SERVICE_BASE_URL + "/admin/access/roles/create/" + newRoleName)
      .then((role: any) => fetchAllRolesAndPrivileges());
  };

  const fetchAllRolesAndPrivileges = () => {
    axios.get(USER_SERVICE_BASE_URL + "/admin/access/roles/all").then((rolesPrivilegesResponse) => {
      setAllRolesPrivilegesList(rolesPrivilegesResponse.data);
      setAllRoles(rolesPrivilegesResponse.data.map((rolePrivilege: any) => rolePrivilege.roleName).sort());
    });
  };

  useEffect(() => {
    fetchAllRolesAndPrivileges();
  }, []);

  return (
    <div>
      <h3>Roles Manager</h3>
      <div style={{ display: "flex" }}>
        <div style={{ width: 400 }}>
          <h5>Roles</h5>
          <List style={{ backgroundColor: navBlue }} disablePadding>
            {allRoles.map((role: string) => (
              <ListItem
                disablePadding
                key={role}
                style={{
                  backgroundColor: activeRole === role ? navBlue : "white",
                  color: activeRole === role ? "white" : "black",
                }}
              >
                <ListItemButton onClick={() => setActiveRole(role)}>
                  <input type="radio" checked={activeRole === role} />
                  <ListItemText>{role}</ListItemText>
                </ListItemButton>
              </ListItem>
            ))}
            <ListItem style={{ backgroundColor: "white" }}>
              <TextField
                size="small"
                variant="standard"
                placeholder="New Role Name"
                value={newRoleName}
                onChange={(event: any) => setNewRoleName(event.target.value)}
              ></TextField>
              <Button
                onClick={() => createNewRole()}
                style={{ marginLeft: 50, width: 100, backgroundColor: "blue", maxWidth: 100, color: "white" }}
              >
                Add
              </Button>
            </ListItem>
            <ListItem style={{ backgroundColor: "white" }}>
              <label style={{ color: "red", marginLeft: 50 }}>{invalidNewRoleNameMessage}</label>
            </ListItem>
          </List>
        </div>
        <div>
          <h5>Privileges</h5>
          <List style={{ backgroundColor: navBlue }} disablePadding>
            {allRolesPrivilegesList
              .filter((rolePriv: any) => rolePriv.roleName === activeRole)
              .map((rolePriv: any) => rolePriv.privilegeNames)
              .flat()
              .map((privName: string) => (
                <ListItem
                  disablePadding
                  key={privName}
                  style={{
                    backgroundColor: activePrivilege === privName ? navBlue : "white",
                    color: activePrivilege === privName ? "white" : "black",
                  }}
                >
                  <ListItemButton onClick={() => setActivePrivilege(privName)}>
                    <input type="radio" checked={activePrivilege === privName} />
                    <ListItemText>{privName}</ListItemText>
                  </ListItemButton>
                </ListItem>
              ))}
            <ListItem style={{ backgroundColor: "white" }}>
              <TextField
                size="small"
                variant="standard"
                placeholder="Privilege to add"
                value={privilegeToAdd}
                onChange={(event: any) => setPrivilegeToAdd(event.target.value)}
              ></TextField>
              <Button
                onClick={() => addPrivilegeToRole()}
                style={{ marginLeft: 50, width: 100, backgroundColor: "blue", maxWidth: 100, color: "white" }}
              >
                Add
              </Button>
            </ListItem>
            <ListItem style={{ backgroundColor: "white", color: "red" }}>
              <label>{invalidPrivilegeNameMessage}</label>
            </ListItem>
          </List>
        </div>
      </div>
    </div>
  );
};
export default RoleManager;
