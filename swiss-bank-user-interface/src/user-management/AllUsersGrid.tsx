import { Table } from "antd";
import axios from "axios";
import { useEffect, useState } from "react";
import SingleUserMilestoneGrid from "./user-profile/SingleUserMilestoneGrid";

const AllUsersGrid = () => {
  const [allUsers, setAllUsers] = useState([]);
  const [expandedRowKeys, setExpandedRowKeys] = useState([] as any[]);
  const BANKING_USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  useEffect(() => {
    axios.get(BANKING_USER_SERVICE_BASE_URL + "/admin/user-profile/all-profiles/user").then(
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

  const handleExpandRows = (expanded: boolean, record: any) => {
    console.log(expandedRowKeys, record);
    if (expanded) {
      expandedRowKeys.push(record.id);
    }
    setExpandedRowKeys([...expandedRowKeys]);
  };

  return (
    <Table
      columns={columns}
      dataSource={allUsers}
      pagination={{ pageSize: 20 }}
      expandable={{
        onExpand: handleExpandRows,
        expandedRowRender: (userRecord: any) => <SingleUserMilestoneGrid userRecord={userRecord} />,
        rowExpandable: (record: any) => record !== "Not Expandable",
      }}
    ></Table>
  );
};

export default AllUsersGrid;
