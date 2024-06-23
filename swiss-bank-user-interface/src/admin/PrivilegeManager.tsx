import axios from "axios";
import { useEffect, useState } from "react";
import ListView from "../utils/list-window/ListView";
import { Button, TextField } from "@mui/material";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";

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
      (role: any) => fetchAllPrivileges(),
      (error) => setInvalidPrivilegeNameMessage(error.response.data.message || error.message)
    );
  };

  useEffect(() => {
    fetchAllPrivileges();
  }, []);
  console.log(allPrivileges);
  return (
    <div style={{ display: "flex" }}>
      <ListView
        listHeader={"Privileges"}
        listItems={[
          {
            key: "no-item",
            render: (item: any) => <Button variant="text">{item.title}</Button>,
            title: (
              <div style={{ display: "flex", padding: 5 }}>
                <AddCircleOutlineIcon style={{ marginRight: 5 }} />
              </div>
            ),
          },
          ...allPrivileges,
        ]}
      />
      <div style={{ justifyContent: "center" }}>
        <div>&lt;</div>
        <div>&lt;&lt;</div>
      </div>
      <ListView
        listHeader={"Privileges"}
        listItems={[
          {
            key: "no-item",
            render: (item: any) => <Button variant="text">{item.title}</Button>,
            title: (
              <div style={{ display: "flex", padding: 5 }}>
                <AddCircleOutlineIcon style={{ marginRight: 5 }} />
              </div>
            ),
          },
          ...allPrivileges,
        ]}
      />
    </div>
  );
};

export default PrivilegeManager;
