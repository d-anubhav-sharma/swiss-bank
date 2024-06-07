import { Table } from "antd";
import VisibilityIcon from "@mui/icons-material/Visibility";
import CheckIcon from "@mui/icons-material/Check";
import ClearIcon from "@mui/icons-material/Clear";
import { formatHumanReadable } from "../../utils/DataFormatter";
import UploadedDocuments from "./UploadedDocuments";

const SingleUserMilestoneGrid = ({ userRecord }: { userRecord: any }) => {
  const MILESTONES_FIELD_LIST = ["email", "phone", "dateOfBirth", "address", "occupation", "photo", "identity"];
  const noButtonStyle = { border: "none", background: "transparent", color: "blue", padding: 0, margin: 0 };

  const renderFieldColumn = (_: any, record: any, __: any) => {
    return formatHumanReadable(record.fieldName);
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
    console.log("check status", field);
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
    console.log(record["fieldName"], record, index);
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
        return <img />;
    }
    return record[record["fieldName"]];
  };

  const viewSubmissionRecord = (value: any, record: any, index: any) => {};

  return (
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
          render: (value, record, index) => (
            <div>
              <button style={noButtonStyle}>
                <CheckIcon style={{ color: "green" }} />
              </button>
              <button style={noButtonStyle}>
                <ClearIcon style={{ color: "red" }} />
              </button>
            </div>
          ),
        },
      ]}
      dataSource={MILESTONES_FIELD_LIST.map((fieldName) => ({ fieldName: fieldName, ...userRecord }))}
      /*
       * { submissions: "Date of Birth" },
       * { submissions: "Nationality" },
       * { submissions: "Address" },
       * { submissions: "Occupation" },
       * { submissions: "Phone" },
       * { submissions: "Government Id" },
       * { submissions: "Email Id" },
       * { submissions: "Personal Photo" }
       */
      pagination={false}
    ></Table>
  );
};

export default SingleUserMilestoneGrid;
