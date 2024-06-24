import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import { Button, Dialog, DialogContent, DialogTitle, FormControl, TextField } from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { setUserMessageBoxState } from "../store/slice";
import ListView from "../utils/list-window/ListView";

const RoleManager = () => {
  const USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  const [allRolesPrivilegesList, setAllRolesPrivilegesList] = useState([]);
  const [allPrivileges, setAllPrivileges] = useState([]);
  const [activeRole, setActiveRole] = useState("");
  const [allRoles, setAllRoles] = useState([]);
  const [newRoleName, setNewRoleName] = useState("");
  const [invalidNewRoleNameMessage, setInvalidNewRoleNameMessage] = useState("");
  const [newRoleDialogOpen, setNewRoleDialogOpen] = useState(false);
  const [privilegeAction, setPrivilegeAction] = useState({} as any);
  const [activePrivilegeFromCurrentTab, setActivePrivilegeFromCurrentTab] = useState("");
  const [activePrivilegeFromAvailableTab, setActivePrivilegeFromAvailableTab] = useState("");
  const dispatch = useDispatch();

  const createNewRole = () => {
    if (!newRoleName || !/^[A-Z_]{3,30}$/.test(newRoleName)) {
      setInvalidNewRoleNameMessage("Invalid new role name: " + newRoleName);
      return;
    }
    setInvalidNewRoleNameMessage("");
    axios.post(USER_SERVICE_BASE_URL + "/admin/access/roles/create/" + newRoleName).then(
      (role: any) => {
        fetchAllRolesAndPrivileges();
        setNewRoleName("");
        setNewRoleDialogOpen(false);
      },
      (error) =>
        dispatch(
          setUserMessageBoxState({
            visible: true,
            level: "error",
            message: error?.response?.data?.message || error.message,
          })
        )
    );
  };

  const fetchAllRolesAndPrivileges = () => {
    axios.get(USER_SERVICE_BASE_URL + "/admin/access/privileges/all").then(
      (privilegesResponse) => {
        setAllPrivileges(privilegesResponse.data.map((priv: any) => priv.privilegeName).flat());
      },
      (error) =>
        dispatch(
          setUserMessageBoxState({
            visible: true,
            level: "error",
            message: error?.response?.data?.message || error.message,
          })
        )
    );
    axios.get(USER_SERVICE_BASE_URL + "/admin/access/roles/all").then(
      (rolesPrivilegesResponse) => {
        setAllRolesPrivilegesList(rolesPrivilegesResponse.data);
        setAllRoles(
          rolesPrivilegesResponse.data.map((rolePrivilege: any) => ({ title: rolePrivilege.roleName })).sort()
        );
      },
      (error) =>
        dispatch(
          setUserMessageBoxState({
            visible: true,
            level: "error",
            message: error?.response?.data?.message || error.message,
          })
        )
    );
  };

  const renderNewRoleDialog = () => {
    return (
      <Dialog open={newRoleDialogOpen} sx={{ width: 600, left: 200 }}>
        <DialogTitle>New Role</DialogTitle>
        <FormControl>
          <DialogContent style={{ display: "flex", flexDirection: "column", width: 400 }}>
            Role Name
            <TextField size="small" value={newRoleName} onChange={(event) => setNewRoleName(event.target.value)} />
            <hr></hr>
            Role Description
            <TextField size="small" multiline rows={3} />
          </DialogContent>
          <div style={{ display: "flex", padding: 10 }}>
            <Button size="small" type="reset" fullWidth onClick={() => setNewRoleDialogOpen(false)}>
              Cancel
            </Button>
            <Button size="small" type="submit" variant="contained" fullWidth onClick={() => createNewRole()}>
              Create
            </Button>
          </div>
          <label style={{ color: "red", margin: 10 }}>{invalidNewRoleNameMessage}</label>
        </FormControl>
      </Dialog>
    );
  };

  const handleSectionChange = (selectedItem: any) => {
    if (typeof selectedItem === "string") setActiveRole(selectedItem);
    else if (typeof selectedItem.title === "string") setActiveRole(selectedItem.title);
    setActivePrivilegeFromCurrentTab("");
  };

  const getAllAvailablePrivileges = () => {
    let alreadyAddedPrivilege = allRolesPrivilegesList
      .filter((rolePriv: any) => rolePriv.roleName === activeRole)
      .map((rolePriv: any) => rolePriv.privilegeNames)
      .flat();
    return allPrivileges
      .filter((privName) => !alreadyAddedPrivilege.includes(privName))
      .map((privName: string) => ({ title: privName }));
  };

  const getAllCurrentPrivileges = () => {
    return allRolesPrivilegesList
      .filter((rolePriv: any) => rolePriv.roleName === activeRole)
      .map((rolePriv: any) => rolePriv.privilegeNames)
      .flat()
      .map((privName: string) => ({ title: privName }));
  };

  const removeSelectedPrivilege = () => {
    axios
      .delete(
        USER_SERVICE_BASE_URL +
          "/admin/access/roles/removePrivilege/" +
          activeRole +
          "/" +
          activePrivilegeFromCurrentTab
      )
      .then(
        (role: any) => {
          fetchAllRolesAndPrivileges();
          setNewRoleName("");
          setNewRoleDialogOpen(false);
        },
        (error) =>
          dispatch(
            setUserMessageBoxState({
              visible: true,
              level: "error",
              message: error?.response?.data?.message || error.message,
            })
          )
      );
  };

  const removeAllPriviliges = () => {
    axios.delete(USER_SERVICE_BASE_URL + "/admin/access/roles/removeAllPriviliges/" + activeRole).then(
      (role: any) => {
        fetchAllRolesAndPrivileges();
        setNewRoleName("");
        setNewRoleDialogOpen(false);
      },
      (error) =>
        dispatch(
          setUserMessageBoxState({
            visible: true,
            level: "error",
            message: error?.response?.data?.message || error.message,
          })
        )
    );
  };

  const addSelectedPrivilege = () => {
    axios
      .post(
        USER_SERVICE_BASE_URL + "/admin/access/roles/addPrivilege/" + activeRole + "/" + activePrivilegeFromAvailableTab
      )
      .then(
        (role: any) => fetchAllRolesAndPrivileges(),
        (error) =>
          dispatch(
            setUserMessageBoxState({
              visible: true,
              level: "error",
              message: error?.response?.data?.message || error.message,
            })
          )
      );
  };

  const addAllPrivileges = () => {
    axios.post(USER_SERVICE_BASE_URL + "/admin/access/roles/addAllPrivileges/" + activeRole).then(
      (role: any) => fetchAllRolesAndPrivileges(),
      (error) =>
        dispatch(
          setUserMessageBoxState({
            visible: true,
            level: "error",
            message: error?.response?.data?.message || error.message,
          })
        )
    );
  };

  useEffect(() => {
    fetchAllRolesAndPrivileges();
  }, []);

  useEffect(() => {
    console.log(getAllCurrentPrivileges(), getAllAvailablePrivileges());
    setPrivilegeAction({
      removeAllPrivilegesDisabled: getAllCurrentPrivileges().length === 0,
      removePrivilegeDisabled: getAllCurrentPrivileges().length === 0 || !activePrivilegeFromCurrentTab,
      addAllPrivilegesDisabled: getAllAvailablePrivileges().length === 0,
      addPrivilegeDisabled: getAllAvailablePrivileges().length === 0 || !activePrivilegeFromAvailableTab,
    });
  }, [
    activeRole,
    allPrivileges,
    allRolesPrivilegesList,
    activePrivilegeFromCurrentTab,
    activePrivilegeFromAvailableTab,
  ]);

  return (
    <div>
      {renderNewRoleDialog()}
      <h3>Roles Manager</h3>
      <div style={{ display: "flex" }}>
        <ListView
          onSelectionChange={handleSectionChange}
          searchEnabled={true}
          listHeader={
            <span>
              {"Roles"}
              <Button onClick={() => setNewRoleDialogOpen(true)}>
                <AddCircleOutlineIcon />
              </Button>
            </span>
          }
          listItems={allRoles}
        />
        <div style={{ display: "flex", marginLeft: 100 }}>
          <ListView
            searchEnabled={true}
            onSelectionChange={(item: any) => {
              if (typeof item === "string") setActivePrivilegeFromAvailableTab(item);
              if (typeof item.title === "string") setActivePrivilegeFromAvailableTab(item.title);
            }}
            listHeader={"Available Privileges"}
            listItems={getAllAvailablePrivileges()}
          />
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              margin: 20,
              marginTop: "30%",
              alignContent: "center",
            }}
          >
            <Button
              variant="contained"
              disabled={privilegeAction.removePrivilegeDisabled}
              onClick={() => removeSelectedPrivilege()}
            >
              &lt;
            </Button>
            <Button
              variant="contained"
              disabled={privilegeAction.removeAllPrivilegesDisabled}
              onClick={() => removeAllPriviliges()}
            >
              &lt;&lt;
            </Button>
            <Button
              variant="contained"
              disabled={privilegeAction.addPrivilegeDisabled}
              onClick={() => addSelectedPrivilege()}
            >
              &gt;
            </Button>
            <Button
              variant="contained"
              disabled={privilegeAction.addAllPrivilegesDisabled}
              onClick={() => addAllPrivileges()}
            >
              &gt;&gt;
            </Button>
          </div>
          <ListView
            searchEnabled={true}
            onSelectionChange={(item: any) => {
              if (typeof item === "string") setActivePrivilegeFromCurrentTab(item);
              if (typeof item.title === "string") setActivePrivilegeFromCurrentTab(item.title);
            }}
            listHeader={"Current  Privileges"}
            listItems={getAllCurrentPrivileges()}
          />
        </div>
      </div>
    </div>
  );
};
export default RoleManager;
