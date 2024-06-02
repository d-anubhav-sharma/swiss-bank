import { Table } from "antd";
import axios from "axios";
import { useEffect, useState } from "react";

const AllUsersGrid = () => {
  const [allUsers, setAllUsers] = useState([]);
  const BANKING_USER_SERVICE_BASE_URL =
    process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  useEffect(() => {
    axios.get(BANKING_USER_SERVICE_BASE_URL + "/user/all").then(
      (fetchAllUsersResponse: any) => {
        setAllUsers(fetchAllUsersResponse.data);
      },
      () => {
        setAllUsers([]);
      }
    );
  }, []);
  const columns = [
    {
      title: "Username",
      dataIndex: "username",
      key: "username",
      width: 210,
    },
    {
      title: "Phone",
      dataIndex: "phone",
      key: "phone",
      width: 210,
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      width: 210,
    },
    {
      title: "Full Name",
      dataIndex: "fullName",
      key: "fullName",
      width: 210,
    },
    {
      title: "Date of Birth",
      dataIndex: "dateOfBirth",
      key: "dateOfBirth",
      render: (value: string) => value?.split("T")[0],
      width: 210,
    },
  ];

  return (
    <Table
      columns={columns}
      dataSource={allUsers}
      pagination={{ pageSize: 20 }}
    ></Table>
  );
};

export default AllUsersGrid;