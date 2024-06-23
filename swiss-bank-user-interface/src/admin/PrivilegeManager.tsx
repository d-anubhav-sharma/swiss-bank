import { Button, List, ListItem, ListItemButton, ListItemText, TextField } from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";

const PrivilegeManager = () => {
  const USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  const [allPrivileges, setAllPrivileges] = useState([]);
  const [activePrivilege, setActivePrivilege] = useState("");
  const [newPrivilegeName, setNewPrivilegeName] = useState("");
  const [invalidPrivilegeNameMessage, setInvalidPrivilegeNameMessage] = useState("");

  const navBlue = "#2c3e50";

  const fetchAllPrivileges = () => {
    axios
      .get(USER_SERVICE_BASE_URL + "/admin/access/privileges/all")
      .then((privilegeResponse) => setAllPrivileges(privilegeResponse.data));
  };

  const createNewPrivilege = () => {
    if (!newPrivilegeName || !/^[A-Z_]{3,30}$/.test(newPrivilegeName)) {
      setInvalidPrivilegeNameMessage("Invalid privilege name: " + newPrivilegeName);
      return;
    }
    setInvalidPrivilegeNameMessage("");
    axios.post(USER_SERVICE_BASE_URL + "/admin/access/privileges/create/" + newPrivilegeName).then(
      (role: any) => fetchAllPrivileges(),
      (error) => setInvalidPrivilegeNameMessage(error.response.data.message || error.message)
    );
  };

  useEffect(() => {
    fetchAllPrivileges();
  }, []);
  return (
    <div>
      <h3>Privileges Manager</h3>
      <div style={{ display: "flex" }}>
        <div style={{ width: 400 }}>
          <h5>Privileges</h5>{" "}
          <List style={{ backgroundColor: navBlue }} disablePadding>
            {allPrivileges.map((privilege: any) => (
              <ListItem
                disablePadding
                key={privilege}
                style={{
                  backgroundColor: activePrivilege === privilege ? navBlue : "white",
                  color: activePrivilege === privilege ? "white" : "black",
                }}
              >
                <ListItemButton onClick={() => setActivePrivilege(privilege)}>
                  <input type="radio" checked={activePrivilege === privilege} />
                  <ListItemText>{privilege.privilegeName}</ListItemText>
                </ListItemButton>
              </ListItem>
            ))}
            <ListItem style={{ backgroundColor: "white" }}>
              <TextField
                size="small"
                variant="standard"
                placeholder="New Privilege Name"
                value={newPrivilegeName}
                onChange={(event: any) => setNewPrivilegeName(event.target.value)}
              ></TextField>
              <Button
                onClick={() => createNewPrivilege()}
                style={{ marginLeft: 50, width: 100, backgroundColor: "blue", maxWidth: 100, color: "white" }}
              >
                Add
              </Button>
            </ListItem>
            <ListItem style={{ backgroundColor: "white" }}>
              <label style={{ color: "red", marginLeft: 50 }}>{invalidPrivilegeNameMessage}</label>
            </ListItem>
          </List>
        </div>
      </div>
    </div>
  );
};

export default PrivilegeManager;
