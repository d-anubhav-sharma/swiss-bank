import { Button } from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { setUserMessageBoxState } from "../store/slice";
import ListView from "../utils/list-window/ListView";

const AccessManager = () => {
  const USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  const [allUsersRolesList, setAllUsersRolesList] = useState([]);
  const [allUsers, setAllUsers] = useState([]);
  const [allRoles, setAllRoles] = useState([]);
  const [activeUser, setActiveUser] = useState("");
  const [roleAction, setRoleAction] = useState({} as any);
  const [activeRoleFromCurrentTab, setActiveRoleFromCurrentTab] = useState("");
  const [activeRoleFromAvailableTab, setActiveRoleFromAvailableTab] = useState("");
  const dispatch = useDispatch();

  const fetchAllUsersAndRoles = () => {
    axios.get(USER_SERVICE_BASE_URL + "/admin/access/roles/all").then(
      (rolesResponse) => {
        setAllRoles(rolesResponse.data.map((role: any) => role.roleName).flat());
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
    axios.get(USER_SERVICE_BASE_URL + "/admin/access/users/all").then(
      (usersRolesResponse) => {
        setAllUsersRolesList(usersRolesResponse.data);
        setAllUsers(usersRolesResponse.data.map((userRole: any) => ({ title: userRole.username })).sort());
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

  const handleSectionChange = (selectedItem: any) => {
    if (typeof selectedItem === "string") setActiveUser(selectedItem);
    else if (typeof selectedItem.title === "string") setActiveUser(selectedItem.title);
    setActiveRoleFromCurrentTab("");
  };

  const getAllAvailableRoles = () => {
    let alreadyAddedRole = allUsersRolesList
      .filter((userRole: any) => userRole.username === activeUser)
      .map((userRole: any) => userRole.roles)
      .flat()
      .map((role: any) => role.roleName);
    console.log(alreadyAddedRole, allRoles);
    return allRoles
      .filter((roleName) => !alreadyAddedRole.includes(roleName))
      .map((roleName: string) => ({ title: roleName }));
  };

  const getAllCurrentRoles = () => {
    return allUsersRolesList
      .filter((userRole: any) => userRole.username === activeUser)
      .map((userRole: any) => userRole.roles)
      .flat()
      .map((role: any) => ({ title: role.roleName }));
  };

  const removeSelectedRole = () => {
    axios
      .delete(USER_SERVICE_BASE_URL + "/admin/access/users/removeRole/" + activeUser + "/" + activeRoleFromCurrentTab)
      .then(
        (role: any) => fetchAllUsersAndRoles(),
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

  const removeAllRoles = () => {
    axios.delete(USER_SERVICE_BASE_URL + "/admin/access/users/removeAllRoles/" + activeUser).then(
      (role: any) => fetchAllUsersAndRoles(),
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

  const addSelectedRole = () => {
    axios
      .post(USER_SERVICE_BASE_URL + "/admin/access/users/addRole/" + activeUser + "/" + activeRoleFromAvailableTab)
      .then(
        (role: any) => fetchAllUsersAndRoles(),
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

  const addAllRoles = () => {
    axios.post(USER_SERVICE_BASE_URL + "/admin/access/users/addAllRoles/" + activeUser).then(
      (role: any) => fetchAllUsersAndRoles(),
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
    fetchAllUsersAndRoles();
  }, []);

  useEffect(() => {
    console.log(getAllCurrentRoles(), getAllAvailableRoles());
    setRoleAction({
      removeAllRolesDisabled: getAllCurrentRoles().length === 0,
      removeRoleDisabled: getAllCurrentRoles().length === 0 || !activeRoleFromCurrentTab,
      addAllRolesDisabled: getAllAvailableRoles().length === 0,
      addRoleDisabled: getAllAvailableRoles().length === 0 || !activeRoleFromAvailableTab,
    });
  }, [activeUser, allRoles, allUsersRolesList, activeRoleFromCurrentTab, activeRoleFromAvailableTab]);

  return (
    <div>
      <h3>Access Manager</h3>
      <div style={{ display: "flex" }}>
        <ListView
          onSelectionChange={handleSectionChange}
          searchEnabled={true}
          listHeader={<span>{"Users"}</span>}
          listItems={allUsers}
        />
        <div style={{ display: "flex", marginLeft: 100 }}>
          <ListView
            searchEnabled={true}
            onSelectionChange={(item: any) => {
              if (typeof item === "string") setActiveRoleFromAvailableTab(item);
              if (typeof item.title === "string") setActiveRoleFromAvailableTab(item.title);
            }}
            listHeader={"Available Roles"}
            listItems={getAllAvailableRoles()}
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
            <Button variant="contained" disabled={roleAction.removeRoleDisabled} onClick={removeSelectedRole}>
              &lt;
            </Button>
            <Button variant="contained" disabled={roleAction.removeAllRolesDisabled} onClick={removeAllRoles}>
              &lt;&lt;
            </Button>
            <Button variant="contained" disabled={roleAction.addRoleDisabled} onClick={addSelectedRole}>
              &gt;
            </Button>
            <Button variant="contained" disabled={roleAction.addAllRolesDisabled} onClick={addAllRoles}>
              &gt;&gt;
            </Button>
          </div>
          <ListView
            searchEnabled={true}
            onSelectionChange={(item: any) => {
              if (typeof item === "string") setActiveRoleFromCurrentTab(item);
              if (typeof item.title === "string") setActiveRoleFromCurrentTab(item.title);
            }}
            listHeader={"Current Roles"}
            listItems={getAllCurrentRoles()}
          />
        </div>
      </div>
    </div>
  );
};
export default AccessManager;
