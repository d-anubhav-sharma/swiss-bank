import { Table } from "antd";
import axios from "axios";
import { useEffect, useState } from "react";
import SingleUserMilestoneGrid from "./user-profile/SingleUserMilestoneGrid";
import { useSelector } from "react-redux";

const AllUsersGrid = () => {
  const { userProfileUpdatedCount } = useSelector((state: any) => state.reducer);
  const [allUsers, setAllUsers] = useState([]);
  const [expandedRowKeys, setExpandedRowKeys] = useState([] as any[]);
  const BANKING_USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  useEffect(() => {
    axios.get(BANKING_USER_SERVICE_BASE_URL + "/admin/user-profile/all-profiles/user").then(
      (fetchAllUsersResponse: any) => {
        setAllUsers(
          fetchAllUsersResponse.data
            .sort((user1: any, user2: any) => (user1.username > user2.username ? 1 : -1))
            .map((userResponseRow: any, index: number) => ({
              ...userResponseRow,
              key: index,
            }))
        );
      },
      () => {
        setAllUsers([]);
      }
    );
  }, [userProfileUpdatedCount]);
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
    if (expanded) {
      setExpandedRowKeys([record.key, ...expandedRowKeys]);
    } else {
      setExpandedRowKeys(expandedRowKeys.filter((key) => key != record.key));
    }
  };

  return (
    <Table
      columns={columns}
      dataSource={allUsers}
      pagination={{ pageSize: 20 }}
      expandable={{
        expandedRowKeys: expandedRowKeys,
        onExpand: handleExpandRows,
        expandedRowRender: (userRecord: any) => <SingleUserMilestoneGrid userRecord={userRecord} />,
        rowExpandable: (record: any) => record !== "Not Expandable",
      }}
    ></Table>
  );
};

export default AllUsersGrid;
