import axios from "axios";
import { useEffect, useState } from "react";
import "./UserProfile.css";
import RefreshIcon from "@mui/icons-material/Refresh";

const UploadedDocuments = ({ fieldImageMap }: { fieldImageMap: any }) => {
  const BANKING_USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  const allRecognizedImages = ["jpg", "img", "png", "jpeg", "jfif"];
  const [allFetchedDocuments, setAllFetchedDocuments] = useState({
    profilePic: {} as any,
    addressProof: {} as any,
    identityProof: {} as any,
    personalPhoto: {} as any,
  });

  const formatHumanReadable = (text: string) => {
    let readableText = text[0].toUpperCase();
    for (let i = 1; i < text.length; i++) {
      if (text[i].charCodeAt(0) < 91) readableText += " ";
      readableText += text[i];
    }
    return readableText;
  };

  const renderFileData = (fileDocument: any) => {
    if (!fileDocument?.fileData) return <div></div>;
    let fileNameSplitted = fileDocument.fileName.split(".");
    let fileExtension = fileNameSplitted[fileNameSplitted.length - 1];
    return (
      <div
        style={{ display: "flex", flexDirection: "column", margin: 20, width: 140, maxWidth: 140, overflow: "hidden" }}
      >
        <a href={"data:image/png;base64," + fileDocument?.fileData} download={fileDocument.fileName}>
          <img
            alt={fileDocument.fileName}
            style={{ width: 100, height: 100 }}
            src={
              allRecognizedImages.includes(fileExtension.toLowerCase())
                ? "data:image/png;base64," + fileDocument?.fileData
                : "pdf_icon.png"
            }
          />
        </a>
        <label>{fileDocument.fileName}</label>
        <label style={{ color: "blue" }}>
          {formatHumanReadable(fileDocument.fileCategory) + " - " + formatHumanReadable(fileDocument.fileSubCategory)}
        </label>
      </div>
    );
  };
  const fetchUploadedDocuments = () => {
    axios.post(`${BANKING_USER_SERVICE_BASE_URL}/file/idList`, fieldImageMap).then(({ data }: any) => {
      setAllFetchedDocuments(data);
    });
  };
  useEffect(() => fetchUploadedDocuments(), []);
  return (
    <section title="Uploaded Documents View">
      <div style={{ display: "flex" }}>
        <span className="h3">Uploaded Documents</span>
        <span>
          <RefreshIcon className="clickable-icon" onClick={fetchUploadedDocuments} />
        </span>
      </div>
      <div style={{ display: "flex" }}>
        {renderFileData(allFetchedDocuments.profilePic)}
        {renderFileData(allFetchedDocuments.addressProof)}
        {renderFileData(allFetchedDocuments.identityProof)}
        {renderFileData(allFetchedDocuments.personalPhoto)}
      </div>
    </section>
  );
};

export default UploadedDocuments;
