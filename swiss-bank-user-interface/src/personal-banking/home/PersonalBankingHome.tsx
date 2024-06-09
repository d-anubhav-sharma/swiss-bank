import axios from "axios";
import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { setUserMessageBoxState } from "../../store/slice";

const PersonalBankingHome = () => {
  const ACCOUNT_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_ACCOUNT_SERVICE_BASE_URL;
  const [personalUserAccounts, setPersonalUserAccounts] = useState([]);
  const dispatch = useDispatch();
  useEffect(() => {
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
  }, []);

  const renderPersonalAccountsList = () => {
    if (!personalUserAccounts?.length) {
      return <span>{"You don't seem to have an account yet. Consider opening one."}</span>;
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
