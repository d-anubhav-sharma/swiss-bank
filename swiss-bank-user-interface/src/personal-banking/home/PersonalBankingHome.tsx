import axios from "axios";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setUserMessageBoxState } from "../../store/slice";

const PersonalBankingHome = () => {
  const ACCOUNT_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_ACCOUNT_SERVICE_BASE_URL;
  const USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  const [personalUserAccounts, setPersonalUserAccounts] = useState([]);
  const [userProfile, setUserProfile] = useState({} as any);
  const { loggedInUser } = useSelector((state: any) => state.reducer);

  const fetchAllUserAccount = () => {
    axios.get(ACCOUNT_SERVICE_BASE_URL + "/personalBanking/Account/all").then(
      (accountListResponse) => {
        setPersonalUserAccounts(accountListResponse.data);
      },
      (error) => {
        dispatch(
          setUserMessageBoxState({
            message: "failed to fetch accounts" + error,
            level: "error",
            visible: true,
          })
        );
      }
    );
  };

  const fetchUserProfile = () => {
    axios.get(`${USER_SERVICE_BASE_URL}/user-profile/username/${loggedInUser}`).then(
      (userProfileResponse) => {
        setUserProfile(userProfileResponse.data);
      },
      (error) => {
        dispatch(
          setUserMessageBoxState({
            message: "failed to fetch user profile" + error,
            level: "error",
            visible: true,
          })
        );
      }
    );
  };

  const dispatch = useDispatch();
  useEffect(() => {
    fetchAllUserAccount();
    fetchUserProfile();
  }, []);

  const renderPersonalAccountsList = () => {
    if (!personalUserAccounts?.length) {
      return (
        <div>
          {(userProfile.governmentIdVerified != "VERIFIED" ||
            userProfile.emailVerified != "VERIFIED" ||
            userProfile.dateofBirthVerified != "VERIFIED" ||
            userProfile.addressVerified != "VERIFIED" ||
            userProfile.occupationVerified != "VERIFIED" ||
            userProfile.phoneVerified != "VERIFIED" ||
            userProfile.photoVerified != "VERIFIED") && (
            <div>
              <table style={{ width: 400 }}>
                <thead>
                  <span style={{ fontSize: 12, color: "orange" }}>
                    <span style={{ color: "red" }}>*</span>Please get these verified before proceeding
                  </span>
                </thead>
                {userProfile.governmentIdVerified != "VERIFIED" && (
                  <tr>
                    <td>Identity</td>
                    <td style={{ color: "red" }}>{userProfile.governmentIdVerified}</td>
                  </tr>
                )}
                {userProfile.emailVerified != "VERIFIED" && (
                  <tr>
                    <td>Email</td>
                    <td style={{ color: "red" }}>{userProfile.emailVerified}</td>
                  </tr>
                )}
                {userProfile.phoneVerified != "VERIFIED" && (
                  <tr>
                    <td>Phone</td>
                    <td style={{ color: "red" }}>{userProfile.phoneVerified}</td>
                  </tr>
                )}
                {userProfile.dateofBirthVerified != "VERIFIED" && (
                  <tr>
                    <td>Date of Birth</td>
                    <td style={{ color: "red" }}>{userProfile.dateofBirthVerified}</td>
                  </tr>
                )}
                {userProfile.addressVerified != "VERIFIED" && (
                  <tr>
                    <td>Address</td>
                    <td style={{ color: "red" }}>{userProfile.addressVerified}</td>
                  </tr>
                )}
                {userProfile.occupationVerified != "VERIFIED" && (
                  <tr>
                    <td>Occupation</td>
                    <td style={{ color: "red" }}>{userProfile.occupationVerified}</td>
                  </tr>
                )}
                {userProfile.photoVerified != "VERIFIED" && (
                  <tr>
                    <td>Photo</td>
                    <td style={{ color: "red" }}>{userProfile.photoVerified}</td>
                  </tr>
                )}
              </table>
              <hr></hr>
            </div>
          )}
          <span>{"You don't seem to have an account yet. Consider opening one."}</span>
        </div>
      );
    }
    return personalUserAccounts.map((account: any) => <div key={account.accountNumber}>{"account"}</div>);
  };
  return (
    <div>
      <h3>Personal Accounts</h3>
      {renderPersonalAccountsList()}
    </div>
  );
};
export default PersonalBankingHome;
