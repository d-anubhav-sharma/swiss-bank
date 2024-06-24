import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import { Button, Dialog, DialogContent, DialogTitle, FormControl, TextField } from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";
import ListView from "../utils/list-window/ListView";

const PrivilegeManager = () => {
  const USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  const [allPrivileges, setAllPrivileges] = useState([]);
  const [newPrivilegeName, setNewPrivilegeName] = useState("");
  const [invalidPrivilegeNameMessage, setInvalidPrivilegeNameMessage] = useState("");
  const [newPrivilegeDialogOpen, setNewPrivilegeDialogOpen] = useState(false);

  const fetchAllPrivileges = () => {
    axios
      .get(USER_SERVICE_BASE_URL + "/admin/access/privileges/all")
      .then((privilegeResponse) =>
        setAllPrivileges(privilegeResponse.data.map((priv: any) => ({ key: priv.id, title: priv.privilegeName })))
      );
  };

  const createNewPrivilege = () => {
    if (!newPrivilegeName || !/^[A-Z_]{3,30}$/.test(newPrivilegeName)) {
      setInvalidPrivilegeNameMessage("Invalid privilege name: " + newPrivilegeName);
      return;
    }
    setInvalidPrivilegeNameMessage("");
    axios.post(USER_SERVICE_BASE_URL + "/admin/access/privileges/create/" + newPrivilegeName).then(
      (role: any) => {
        fetchAllPrivileges();
        setNewPrivilegeDialogOpen(false);
        setNewPrivilegeName("");
      },
      (error) => {
        setInvalidPrivilegeNameMessage(error.response.data.message || error.message);
      }
    );
  };

  const renderNewPrivilegeDialog = () => {
    return (
      <Dialog open={newPrivilegeDialogOpen} sx={{ width: 600, left: 200 }}>
        <DialogTitle>New privilege</DialogTitle>
        <FormControl>
          <DialogContent style={{ display: "flex", flexDirection: "column", width: 400 }}>
            Privilege Name
            <TextField
              size="small"
              value={newPrivilegeName}
              onChange={(event) => setNewPrivilegeName(event.target.value)}
            />
            <hr></hr>
            Privilege Description
            <TextField size="small" multiline rows={3} />
          </DialogContent>
          <div style={{ display: "flex", padding: 10 }}>
            <Button size="small" type="reset" fullWidth onClick={() => setNewPrivilegeDialogOpen(false)}>
              Cancel
            </Button>
            <Button size="small" type="submit" variant="contained" fullWidth onClick={() => createNewPrivilege()}>
              Create
            </Button>
          </div>
          <label style={{ color: "red", margin: 10 }}>{invalidPrivilegeNameMessage}</label>
        </FormControl>
      </Dialog>
    );
  };

  useEffect(() => {
    fetchAllPrivileges();
  }, []);
  return (
    <div style={{ display: "flex" }}>
      <ListView
        searchEnabled={true}
        listHeader={
          <span>
            {"Privileges"}
            <Button onClick={() => setNewPrivilegeDialogOpen(true)}>
              <AddCircleOutlineIcon />
            </Button>
          </span>
        }
        listItems={allPrivileges}
      />
      {renderNewPrivilegeDialog()}
    </div>
  );
};

export default PrivilegeManager;
