import { Table } from "antd";
import CheckIcon from "@mui/icons-material/Check";
import ClearIcon from "@mui/icons-material/Clear";
import { formatHumanReadable } from "../../utils/DataFormatter";
import UploadedDocuments from "./UploadedDocuments";
import ConfirmationDialogBox from "../../user-message-box/ConfirmationDialogBox";
import { useDispatch, useSelector } from "react-redux";
import { setConfirmationDialogBoxState, setUserProfileUpdatedCount } from "../../store/slice";
import axios from "axios";

const SingleUserMilestoneGrid = ({ userRecord }: { userRecord: any }) => {
  const BANKING_USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  const dispatch = useDispatch();
  const { confirmationDialogBoxState, userProfileUpdatedCount } = useSelector((state: any) => state.reducer);
  const MILESTONES_FIELD_LIST = ["email", "phone", "dateOfBirth", "address", "occupation", "photo", "identity"];
  const noButtonStyle = {
    border: "none",
    background: "transparent",
    color: "blue",
    padding: 0,
    margin: 0,
  };

  const renderFieldColumn = (_: any, record: any, __: any) => {
    return formatHumanReadable(record.fieldName);
  };

  const updateApprovalStatus = (username: string, fieldName: string, status: string) => {
    axios.post(BANKING_USER_SERVICE_BASE_URL + "/approval/profileFields", {
      fieldName: fieldName,
      username: username,
      status: status,
    });
  };

  const checkStatus = (field: any) => {
    switch (field) {
      case "NOT_PROCESSED":
        return <span style={{ color: "orange" }}>Not Processed</span>;
      case "VERIFIED":
        return <span style={{ color: "green" }}>Verified</span>;
      case "REJECTED":
        return <span style={{ color: "red" }}>Rejected</span>;
    }
    return field === "NOT_PROCESSED" ? "Completed" : "Incomplete";
  };
  const renderStatusColumn = (value: any, record: any, index: any) => {
    switch (record.fieldName) {
      case "email":
        return checkStatus(record.emailVerified);
      case "phone":
        return checkStatus(record.phoneVerified);
      case "dateOfBirth":
        return checkStatus(record.dateofBirthVerified);
      case "address":
        return checkStatus(record.addressVerified);
      case "occupation":
        return checkStatus(record.occupationVerified);
      case "photo":
        return checkStatus(record.photoVerified);
      case "identity":
        return checkStatus(record.governmentIdVerified);
    }
  };

  const renderValueColumn = (value: any, record: any, index: any) => {
    switch (record["fieldName"]) {
      case "email":
        return record.kyc.emailForVerification;
      case "phone":
        return record.kyc.phoneForVerification;
      case "dateOfBirth":
        return record.basicInfo.dateOfBirth.split("T")[0];
      case "address":
        return (
          record.address.addressLine1 +
          "\n" +
          record.address.addressLine2 +
          "\n" +
          record.address.addressLine3 +
          "\n" +
          record.address.landMark +
          "\n" +
          record.address.city +
          "-" +
          record.address.pincode
        );
      case "occupation":
        return "Title: " + record.occupation.jobs[0].title + "\nCompany: " + record.occupation.jobs[0].company;
      case "photo":
        return "";
    }
    return record[record["fieldName"]];
  };

  const rejectFieldDataSubmission = (record: any, value: any, index: any) => {
    dispatch(
      setConfirmationDialogBoxState({
        ...confirmationDialogBoxState,
        open: true,
        onclose: () => {
          dispatch(setConfirmationDialogBoxState({ open: false }));
        },
        actionReject: () => {
          dispatch(setConfirmationDialogBoxState({ open: false }));
        },
        actionConfirm: () => {
          dispatch(setConfirmationDialogBoxState({ open: false }));
          updateApprovalStatus(record.username, record.fieldName, "REJECTED");
          dispatch(setUserProfileUpdatedCount(userProfileUpdatedCount + 1));
        },
        title: (
          <span>
            <ClearIcon style={{ color: "red" }} />
            {"Reject"}
          </span>
        ),
        message: "Do you really want to reject the submission",
      })
    );
  };
  const acceptFieldDataSubmission = (record: any, value: any, index: any) => {
    dispatch(
      setConfirmationDialogBoxState({
        ...confirmationDialogBoxState,
        open: true,
        onclose: () => {
          dispatch(setConfirmationDialogBoxState({ open: false }));
        },
        actionReject: () => {
          dispatch(setConfirmationDialogBoxState({ open: false }));
        },
        actionConfirm: () => {
          updateApprovalStatus(record.username, record.fieldName, "VERIFIED");
          dispatch(setConfirmationDialogBoxState({ open: false }));
          dispatch(setUserProfileUpdatedCount(userProfileUpdatedCount + 1));
        },
        title: (
          <span>
            <CheckIcon style={{ color: "green" }} />
            {"Approve"}
          </span>
        ),
        message: (
          <div>
            <hr />
            Please confirm to approve the field {record.fieldName} for user {record.username}
            <br />
            {record.basicInfo.email}
          </div>
        ),
      })
    );
  };

  return (
    <>
      <Table
        style={{ marginLeft: 100, whiteSpace: "pre" }}
        footer={() => (
          <UploadedDocuments
            fieldImageMap={{
              profilePic: userRecord.basicInfo.profilePic,
              identityProof: userRecord.kyc.identityProof,
              addressProof: userRecord.kyc.addressProof,
              personalPhoto: userRecord.kyc.personalPhoto,
            }}
          />
        )}
        columns={[
          { title: "Submissions", dataIndex: "submissions", key: "submissions", render: renderFieldColumn },
          {
            title: "Value",
            dataIndex: "value",
            key: "value",
            render: renderValueColumn,
          },
          {
            title: "Status",
            dataIndex: "status",
            key: "status",
            render: renderStatusColumn,
          },
          {
            title: "Action",
            dataIndex: "action",
            key: "action",
            render: (value, record, index) => {
              return (
                <div>
                  <button style={{ ...noButtonStyle }} onClick={() => acceptFieldDataSubmission(record, value, index)}>
                    <CheckIcon style={{ color: "green" }} />
                  </button>
                  <button style={{ ...noButtonStyle }}>
                    <ClearIcon
                      style={{ color: "red" }}
                      onClick={() => rejectFieldDataSubmission(record, value, index)}
                    />
                  </button>
                </div>
              );
            },
          },
        ]}
        dataSource={MILESTONES_FIELD_LIST.map((fieldName) => ({ fieldName: fieldName, ...userRecord }))}
        pagination={false}
      ></Table>
      <ConfirmationDialogBox />
    </>
  );
};

export default SingleUserMilestoneGrid;
