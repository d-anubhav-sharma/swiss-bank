import React, { useEffect, useState } from "react";
import "./UserProfile.css";
import RefreshIcon from "@mui/icons-material/Refresh";
import ALL_NATIONS_LIST from "../../utils/AllNationsList";
import PhoneInput from "react-phone-input-2";
import "react-phone-input-2/lib/style.css";

import {
  Checkbox,
  FormControl,
  FormControlLabel,
  FormGroup,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import { ALL_ADDRESS_PROOF_TYPES, ALL_IDENTITY_PROOF_TYPES } from "../../utils/AllDocsList";
import ALL_GENDERS_LIST from "../../utils/Gender";
import axios from "axios";
import { setUserMessageBoxState } from "../../store/slice";
import { useDispatch, useSelector } from "react-redux";
import UploadedDocuments from "./UploadedDocuments";

const UserProfile = () => {
  const BANKING_USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;

  const dispatch = useDispatch();
  const { loggedInUser } = useSelector((state: any) => state.reducer);
  const refreshUserProfile = () => {
    axios.get(`${BANKING_USER_SERVICE_BASE_URL}/user-profile/username/${loggedInUser}`).then(
      (userProfileResponse) => {
        userProfileResponse.data.basicInfo.dateOfBirth = userProfileResponse.data.basicInfo.dateOfBirth?.split("T")[0];
        userProfileResponse.data.occupation = {
          title0: userProfileResponse.data.occupation.jobs?.[0].title,
          company0: userProfileResponse.data.occupation.jobs?.[0].company,
          companyAddress0: userProfileResponse.data.occupation.jobs?.[0].companyAddress,
          companySalary0: userProfileResponse.data.occupation.jobs?.[0].salary,
          companyPincode0: userProfileResponse.data.occupation.jobs?.[0].pincode,
          title1: userProfileResponse.data.occupation.jobs?.[1].title,
          company1: userProfileResponse.data.occupation.jobs?.[1].company,
          companyAddress1: userProfileResponse.data.occupation.jobs?.[1].companyAddress,
          companySalary1: userProfileResponse.data.occupation.jobs?.[1].salary,
          companyPincode1: userProfileResponse.data.occupation.jobs?.[1].pincode,
          title2: userProfileResponse.data.occupation.jobs?.[2].title,
          company2: userProfileResponse.data.occupation.jobs?.[2].company,
          companyAddress2: userProfileResponse.data.occupation.jobs?.[2].companyAddress,
          companySalary2: userProfileResponse.data.occupation.jobs?.[2].salary,
          companyPincode2: userProfileResponse.data.occupation.jobs?.[2].pincode,
        };
        userProfileResponse.data.consent = {
          canSendBankingOffers: userProfileResponse.data.consent.canSendBankingOffers ? "on" : "off",
          canSendOtherServiceOffers: userProfileResponse.data.consent.canSendOtherServiceOffers ? "on" : "off",
          canUseDataForTrainingPurpose: userProfileResponse.data.consent.canUseDataForTrainingPurpose ? "on" : "off",
          canShareDataWithThirdParty: userProfileResponse.data.consent.canShareDataWithThirdParty ? "on" : "off",
          canSendPromotionalAdds: userProfileResponse.data.consent.canSendPromotionalAdds ? "on" : "off",
          canKeepDataForMoreThan180Days: userProfileResponse.data.consent.canKeepDataForMoreThan180Days ? "on" : "off",
        };
        setProfileData(userProfileResponse.data);
      },
      () => {}
    );
  };

  const saveUserProfileData = (userProfileUpdatePayload: any) => {
    axios.post(BANKING_USER_SERVICE_BASE_URL + "/user-profile/save", userProfileUpdatePayload).then(
      (responseProfile) => {
        dispatch(
          setUserMessageBoxState({
            message: "Profile saved successfully with id" + responseProfile.data.id,
            level: "success",
            visible: true,
          })
        );
      },
      (error) => {
        dispatch(
          setUserMessageBoxState({
            message: "Failed to save profile " + error,
            level: "error",
            visible: true,
          })
        );
      }
    );
  };

  const validateAndSendFile = (event: any, section: string, field: string) => {
    const fileData = new FormData();
    fileData.append("file", new Blob([event.target.files[0]]), event.target.files[0].name);
    axios
      .post(`${BANKING_USER_SERVICE_BASE_URL}/file/save?category=${section}&subCategory=${field}`, fileData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then(
        (responseData) => {
          profileData[section as keyof Object][field as keyof Object] = responseData.data.fileId;
          handleInputChange(section, field);
        },
        (error) => {
          dispatch(
            setUserMessageBoxState({
              message: "user registration failed: " + error?.response?.data?.message,
              level: "error",
              visible: true,
            })
          );
        }
      );
  };
  const handleUserProfileUpdate = () => {
    let userProfileUpdatePayload = {
      basicInfo: {
        username: loggedInUser,
        firstName: profileData.basicInfo.firstName,
        lastName: profileData.basicInfo.lastName,
        fullName: profileData.basicInfo.firstName + " " + profileData.basicInfo.lastName,
        email: profileData.basicInfo.email,
        dateOfBirth: profileData.basicInfo.dateOfBirth,
        age: getAge(),
        gender: profileData.basicInfo.gender,
        phone: profileData.basicInfo.phone,
        secondaryEmail: profileData.basicInfo.secondaryEmail,
        secondaryPhone: profileData.basicInfo.secondaryPhone,
        nationality: profileData.basicInfo.nationality,
        profilePic: profileData.basicInfo.profilePic,
      },
      address: {
        username: loggedInUser,
        addressLine1: profileData.address.addressLine1,
        addressLine2: profileData.address.addressLine2,
        addressLine3: profileData.address.addressLine3,
        city: profileData.address.city,
        state: profileData.address.state,
        pincode: profileData.address.pincode,
        country: profileData.address.country,
      },
      occupation: {
        username: loggedInUser,
        jobs: [
          {
            username: loggedInUser,
            title: profileData.occupation.title0,
            company: profileData.occupation.company0,
            companyAddress: profileData.occupation.companyAddress0,
            salary: profileData.occupation.companySalary0,
            pincode: profileData.occupation.companyPincode0,
          },
          {
            username: loggedInUser,
            title: profileData.occupation.title1,
            company: profileData.occupation.company1,
            companyAddress: profileData.occupation.companyAddress1,
            salary: profileData.occupation.companySalary1,
            pincode: profileData.occupation.companyPincode1,
          },
          {
            username: loggedInUser,
            title: profileData.occupation.title2,
            company: profileData.occupation.company2,
            companyAddress: profileData.occupation.companyAddress2,
            salary: profileData.occupation.companySalary2,
            pincode: profileData.occupation.companyPincode2,
          },
        ],
      },
      kyc: {
        username: loggedInUser,
        addressProofType: profileData.kyc.addressProofType,
        addressProof: profileData.kyc.addressProof,
        identityProofType: profileData.kyc.identityProofType,
        identityProof: profileData.kyc.identityProof,
        personalPhoto: profileData.kyc.personalPhoto,
        emailForVerification: profileData.kyc.emailForVerification,
        phoneForVerification: profileData.kyc.phoneForVerification,
      },
      consent: {
        username: loggedInUser,
        canSendBankingOffers: profileData.consent.canSendBankingOffers == "on",
        canSendOtherServiceOffers: profileData.consent.canSendOtherServiceOffers == "on",
        canUseDataForTrainingPurpose: profileData.consent.canUseDataForTrainingPurpose == "on",
        canShareDataWithThirdParty: profileData.consent.canShareDataWithThirdParty == "on",
        canSendPromotionalAdds: profileData.consent.canSendPromotionalAdds == "on",
        canKeepDataForMoreThan180Days: profileData.consent.canKeepDataForMoreThan180Days == "on",
      },
    };
    saveUserProfileData(userProfileUpdatePayload);
  };

  const getAge = () => {
    let ageDate = new Date(profileData.basicInfo.dateOfBirth);
    let currentDate = new Date();
    let diffYear = currentDate.getFullYear() - ageDate.getFullYear();
    if (currentDate.getMonth() - ageDate.getMonth() < 0) {
      diffYear -= 1;
    }
    return diffYear || 0;
  };

  useEffect(() => {
    const unloadCallback = (event: any) => {
      event.preventDefault();
      event.returnValue = "";
      return "";
    };

    window.addEventListener("beforeunload", unloadCallback);
    return () => window.removeEventListener("beforeunload", unloadCallback);
  }, []);

  useEffect(() => {
    refreshUserProfile();
  }, []);
  const [profileData, setProfileData] = useState({
    basicInfo: {
      firstName: "",
      lastName: "",
      fullName: "",
      email: "",
      dateOfBirth: "",
      age: "",
      gender: "MALE",
      phone: "91",
      secondaryEmail: "",
      secondaryPhone: "91",
      nationality: "",
      profilePic: "",
    },
    address: {
      addressLine1: "",
      addressLine2: "",
      addressLine3: "",
      city: "",
      state: "",
      pincode: "",
      country: "",
    },
    occupation: {
      title0: "",
      company0: "",
      companyAddress0: "",
      companySalary0: "",
      companyPincode0: "",
      title1: "",
      company1: "",
      companyAddress1: "",
      companySalary1: "",
      companyPincode1: "",
      title2: "",
      company2: "",
      companyAddress2: "",
      companySalary2: "",
      companyPincode2: "",
    },
    kyc: {
      addressProofType: "",
      addressProof: "",
      identityProofType: "",
      identityProof: "",
      personalPhoto: "",
      emailForVerification: "",
      phoneForVerification: "91",
    },
    consent: {
      canSendBankingOffers: "off",
      canSendOtherServiceOffers: "off",
      canUseDataForTrainingPurpose: "off",
      canShareDataWithThirdParty: "off",
      canSendPromotionalAdds: "off",
      canKeepDataForMoreThan180Days: "off",
    },
  });

  const handleInputChangeValue = (section: any, field: any) => (value: any) => {
    setProfileData((prevData: any) => ({
      ...prevData,
      [section]: {
        ...prevData[section],
        [field]: value,
      },
    }));
  };

  const handleInputChange = (section: any, field: any) => (e: any) => {
    const value = e.target.value;
    setProfileData((prevData: any) => ({
      ...prevData,
      [section]: {
        ...prevData[section],
        [field]: value,
      },
    }));
  };

  return (
    <div className="user-profile">
      <div style={{ display: "flex" }}>
        <span className="h2">User Profile</span>
        <span>
          <RefreshIcon className="clickable-icon" onClick={refreshUserProfile} />
        </span>
      </div>
      <ol style={{ color: "red" }}>
        <li>
          Please be careful while filling KYC section. All the informations provided in KYC section would be used for
          background verification and other regulatory requirements. User will be responsible for any legal action
          because of wrong data submitted here. Any such wrong submission intentional/non-intenional will be prosecuted
          as per local authorities rules/guidelines
        </li>
        <li>All Documents submitted in the entire form should be in range of 20KB-1MB</li>
        <li>
          Occupation section should be filled in decreasing order of chronology. Put your latest organization data
          first.
        </li>
      </ol>
      <section className="form-columns" title="Basic Info" id="basicInfo">
        <h3>Basic Info</h3>
        <div></div>
        <TextField
          id="firstName"
          label="First Name"
          variant="outlined"
          value={profileData.basicInfo.firstName}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("basicInfo", "firstName")}
          required
        />
        <TextField
          id="lastName"
          label="Last Name"
          variant="outlined"
          value={profileData.basicInfo.lastName}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("basicInfo", "lastName")}
          required
        />
        <TextField
          id="fullName"
          label="Full Name"
          variant="outlined"
          value={profileData.basicInfo.firstName + " " + profileData.basicInfo.lastName}
          InputLabelProps={{ shrink: true }}
          disabled
        />
        <TextField
          id="email"
          label="Email"
          variant="outlined"
          value={profileData.basicInfo.email}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("basicInfo", "email")}
          type="email"
          required
        />
        <TextField
          id="dateOfBirth"
          variant="outlined"
          value={profileData.basicInfo.dateOfBirth}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("basicInfo", "dateOfBirth")}
          type="date"
          label="Date of Birth"
        />
        <TextField
          id="age"
          variant="outlined"
          value={getAge()}
          type="text"
          disabled
          InputLabelProps={{ shrink: true }}
          label="Age"
        />
        <FormControl fullWidth>
          <InputLabel id="gender-label">Gender</InputLabel>
          <Select
            labelId="gender-label"
            id="gender"
            value={profileData.basicInfo.gender}
            label="gender"
            onChange={handleInputChange("basicInfo", "gender")}
          >
            {ALL_GENDERS_LIST.map((gender: any) => (
              <MenuItem key={gender.value} value={gender.value}>
                {gender.label}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <PhoneInput
          specialLabel="Phone"
          country={"IND"}
          value={profileData.basicInfo.phone}
          onChange={handleInputChangeValue("basicInfo", "phone")}
        />
        <PhoneInput
          specialLabel="Secondary Phone"
          country={"IND"}
          value={profileData.basicInfo.secondaryPhone}
          onChange={handleInputChangeValue("basicInfo", "secondaryPhone")}
        />
        <TextField
          id="secondaryEmail"
          label="Secondary Email"
          variant="outlined"
          type="email"
          value={profileData.basicInfo.secondaryEmail}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("basicInfo", "secondaryEmail")}
        />
        <FormControl fullWidth>
          <InputLabel id="nationality-label">Nationality</InputLabel>
          <Select
            labelId="nationality-label"
            id="nationality"
            value={profileData.basicInfo.nationality}
            label="Nationality"
            onChange={handleInputChange("basicInfo", "nationality")}
          >
            {ALL_NATIONS_LIST.map((nation: any) => (
              <MenuItem key={nation.value} value={nation.value}>
                {nation.label}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <TextField
          id="profilePic"
          variant="outlined"
          type="file"
          InputLabelProps={{ shrink: true }}
          label="Profile Pic"
          onChange={(event) => validateAndSendFile(event, "basicInfo", "profilePic")}
        />
      </section>

      <section className="form-columns" title="Address" id="address">
        <h3>Address</h3>
        <div></div>
        <TextField
          id="addressLine1"
          label="Address Line 1"
          variant="outlined"
          value={profileData.address.addressLine1}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("address", "addressLine1")}
        />
        <TextField
          id="addressLine2"
          label="Address Line 2"
          variant="outlined"
          value={profileData.address.addressLine2}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("address", "addressLine2")}
        />
        <TextField
          id="addressLine3"
          label="Address Line 3"
          variant="outlined"
          value={profileData.address.addressLine3}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("address", "addressLine3")}
        />
        <TextField
          id="city"
          label="City"
          variant="outlined"
          value={profileData.address.city}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("address", "city")}
        />
        <TextField
          id="state"
          label="State"
          variant="outlined"
          value={profileData.address.state}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("address", "state")}
        />
        <TextField
          id="pincode"
          label="Pin Code"
          variant="outlined"
          value={profileData.address.pincode}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("address", "pincode")}
        />
        <TextField
          id="country"
          label="Country"
          variant="outlined"
          value={profileData.address.country}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("address", "country")}
        />
      </section>
      <section className="form-columns" title="Occupation" id="occupation">
        <h3>Occupation</h3>
        <div></div>
        <TextField
          id="company0"
          label="Recent Company"
          variant="outlined"
          value={profileData.occupation.company0}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "company0")}
        />
        <TextField
          id="title0"
          label="Job Title"
          variant="outlined"
          value={profileData.occupation.title0}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "title0")}
        />
        <TextField
          id="companyPincode0"
          label="Pin Code"
          variant="outlined"
          value={profileData.occupation.companyPincode0}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "companyPincode0")}
        />
        <TextField
          id="companyAddress0"
          label="Address"
          variant="outlined"
          value={profileData.occupation.companyAddress0}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "companyAddress0")}
        />
        <TextField
          id="companySalary0"
          label="Salary"
          variant="outlined"
          type="number"
          value={profileData.occupation.companySalary0}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "companySalary0")}
        />
        <div></div>
        <hr style={{ width: "100%" }}></hr>
        <hr style={{ width: "100%" }}></hr>
        <TextField
          id="company1"
          label="Past Company 1"
          variant="outlined"
          value={profileData.occupation.company1}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "company1")}
        />
        <TextField
          id="title1"
          label="Job Title"
          variant="outlined"
          value={profileData.occupation.title1}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "title1")}
        />
        <TextField
          id="companyPincode1"
          label="Pin Code"
          variant="outlined"
          value={profileData.occupation.companyPincode1}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "companyPincode1")}
        />
        <TextField
          id="companyAddress1"
          label="Address"
          variant="outlined"
          value={profileData.occupation.companyAddress1}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "companyAddress1")}
        />
        <TextField
          id="companySalary1"
          label="Salary"
          variant="outlined"
          type="number"
          value={profileData.occupation.companySalary1}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "companySalary1")}
        />
        <div></div>
        <hr style={{ width: "100%" }}></hr>
        <hr style={{ width: "100%" }}></hr>
        <TextField
          id="company2"
          label="Past Company 2"
          variant="outlined"
          value={profileData.occupation.company2}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "company2")}
        />
        <TextField
          id="title2"
          label="Job Title"
          variant="outlined"
          value={profileData.occupation.title2}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "title2")}
        />
        <TextField
          id="companyPincode2"
          label="Pin Code"
          variant="outlined"
          value={profileData.occupation.companyPincode2}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "companyPincode2")}
        />
        <TextField
          id="companyAddress2"
          label="Address"
          variant="outlined"
          value={profileData.occupation.companyAddress2}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "companyAddress2")}
        />
        <TextField
          id="companySalary2"
          label="Salary"
          variant="outlined"
          type="number"
          value={profileData.occupation.companySalary2}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("occupation", "companySalary2")}
        />
        <div></div>
      </section>
      <section className="form-columns" title="kyc" id="kyc">
        <h3>
          KYC<sup style={{ color: "red" }}>*</sup>
        </h3>
        <div></div>
        <FormControl fullWidth>
          <InputLabel id="addressProofType-label">Address Proof Type</InputLabel>
          <Select
            labelId="addressProofType-label"
            id="addressProofType"
            value={profileData.kyc.addressProofType}
            label="Address Proof Type"
            onChange={handleInputChange("kyc", "addressProofType")}
          >
            {ALL_ADDRESS_PROOF_TYPES.map((addressType: any) => (
              <MenuItem key={addressType.value} value={addressType.value}>
                {addressType.label}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <TextField
          id="addressProof"
          variant="outlined"
          type="file"
          InputLabelProps={{ shrink: true }}
          label="Address Proof"
          onChange={(event) => validateAndSendFile(event, "kyc", "addressProof")}
        />
        <FormControl fullWidth>
          <InputLabel id="identityProofType-label">Identity Proof Type</InputLabel>
          <Select
            labelId="identityProofType-label"
            id="identityProofType"
            value={profileData.kyc.identityProofType}
            label="Identity Proof Type"
            onChange={handleInputChange("kyc", "identityProofType")}
          >
            {ALL_IDENTITY_PROOF_TYPES.map((identityType: any) => (
              <MenuItem key={identityType.value} value={identityType.value}>
                {identityType.label}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <TextField
          id="identityProof"
          variant="outlined"
          type="file"
          InputLabelProps={{ shrink: true }}
          label="Identity Proof"
          onChange={(event) => validateAndSendFile(event, "kyc", "identityProof")}
        />
        <TextField
          id="emailForVerification"
          label="Email for verification"
          variant="outlined"
          value={profileData.kyc.emailForVerification}
          InputLabelProps={{ shrink: true }}
          onChange={handleInputChange("kyc", "emailForVerification")}
        />
        <TextField
          id="personalPhoto"
          variant="outlined"
          type="file"
          InputLabelProps={{ shrink: true }}
          label="Personal Photo"
          onChange={(event) => validateAndSendFile(event, "kyc", "personalPhoto")}
        />
        <PhoneInput
          specialLabel="Phone for verification"
          country={"IND"}
          value={profileData.kyc.phoneForVerification}
          onChange={handleInputChangeValue("kyc", "phoneForVerification")}
        />
      </section>
      <section title="Consent" id="consent">
        <h3>Consent</h3>
        <div></div>
        <FormGroup>
          <FormControlLabel
            className="consent-form-checkbox-label"
            control={
              <Checkbox
                id="canSendBankingOffers"
                value={profileData.consent.canSendBankingOffers}
                checked={profileData.consent.canSendBankingOffers == "on"}
                onChange={() =>
                  handleInputChangeValue(
                    "consent",
                    "canSendBankingOffers"
                  )(profileData.consent.canSendBankingOffers == "on" ? "off" : "on")
                }
              />
            }
            label="Allow Swiss Bank to send personal banking offers"
          />
          <FormControlLabel
            className="consent-form-checkbox-label"
            control={
              <Checkbox
                id="canSendOtherServiceOffers"
                value={profileData.consent.canSendOtherServiceOffers}
                checked={profileData.consent.canSendOtherServiceOffers == "on"}
                onChange={() =>
                  handleInputChangeValue(
                    "consent",
                    "canSendOtherServiceOffers"
                  )(profileData.consent.canSendOtherServiceOffers == "on" ? "off" : "on")
                }
              />
            }
            label="Allow Swiss Bank to send other service details/offers "
          />
          <FormControlLabel
            className="consent-form-checkbox-label"
            control={
              <Checkbox
                id="canUseDataForTrainingPurpose"
                value={profileData.consent.canUseDataForTrainingPurpose}
                checked={profileData.consent.canUseDataForTrainingPurpose == "on"}
                onChange={() =>
                  handleInputChangeValue(
                    "consent",
                    "canUseDataForTrainingPurpose"
                  )(profileData.consent.canUseDataForTrainingPurpose == "on" ? "off" : "on")
                }
              />
            }
            label="Allow Swiss Bank to use populated data for training and quality purpose"
          />
          <FormControlLabel
            className="consent-form-checkbox-label"
            control={
              <Checkbox
                id="canShareDataWithThirdParty"
                value={profileData.consent.canShareDataWithThirdParty}
                checked={profileData.consent.canShareDataWithThirdParty == "on"}
                onChange={() =>
                  handleInputChangeValue(
                    "consent",
                    "canShareDataWithThirdParty"
                  )(profileData.consent.canShareDataWithThirdParty == "on" ? "off" : "on")
                }
              />
            }
            label="Allow Swiss Bank to share the submitted data with their vendors and other third parties"
          />
          <FormControlLabel
            className="consent-form-checkbox-label"
            control={
              <Checkbox
                id="canSendPromotionalAdds"
                value={profileData.consent.canSendPromotionalAdds}
                checked={profileData.consent.canSendPromotionalAdds == "on"}
                onChange={() =>
                  handleInputChangeValue(
                    "consent",
                    "canSendPromotionalAdds"
                  )(profileData.consent.canSendPromotionalAdds == "on" ? "off" : "on")
                }
              />
            }
            label="Allow Swiss Bank to send promotional adds"
          />
          <FormControlLabel
            className="consent-form-checkbox-label"
            control={
              <Checkbox
                id="canKeepDataForMoreThan180Days"
                value={profileData.consent.canKeepDataForMoreThan180Days}
                checked={profileData.consent.canKeepDataForMoreThan180Days == "on"}
                onChange={() =>
                  handleInputChangeValue(
                    "consent",
                    "canKeepDataForMoreThan180Days"
                  )(profileData.consent.canKeepDataForMoreThan180Days == "on" ? "off" : "on")
                }
              />
            }
            label="Allow Swiss Bank to keep user related data for more than 180 days"
          />
        </FormGroup>
      </section>
      {profileData.basicInfo.profilePic && (
        <UploadedDocuments
          fieldImageMap={{
            profilePic: profileData.basicInfo.profilePic,
            identityProof: profileData.kyc.identityProof,
            addressProof: profileData.kyc.addressProof,
            personalPhoto: profileData.kyc.personalPhoto,
          }}
        />
      )}
      <button onClick={() => handleUserProfileUpdate()}>Submit</button>
    </div>
  );
};

export default UserProfile;
