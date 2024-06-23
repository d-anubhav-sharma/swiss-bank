import LoginForm from "./login-form/LoginForm";
import Navbar from "./main-navbar/Navbar";
import RegisterForm from "./register-form/RegisterForm";

import { Elements } from "@stripe/react-stripe-js";
import { loadStripe } from "@stripe/stripe-js";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import About from "./about/About";
import AccessManager from "./admin/AccessManager";
import Admin from "./admin/Admin";
import PrivilegeManager from "./admin/PrivilegeManager";
import RoleManager from "./admin/RoleManager";
import Contact from "./contact/Contact";
import Home from "./home/Home";
import PersonalBankingCreateAccount from "./personal-banking/account/PersonalBankingCreateAccount";
import PersonalBankingHome from "./personal-banking/home/PersonalBankingHome";
import PersonalBankingAllTransactions from "./personal-banking/payment/AllTransactions";
import PaymentForm from "./personal-banking/payment/PaymentForm";
import Services from "./services/Services";
import { setActiveContentPage } from "./store/slice";
import AllUsersGrid from "./user-management/AllUsersGrid";
import UserProfile from "./user-management/user-profile/UserProfile";
import ProgressItemsBar from "./user-message-box/ProgressItemsBar";
import UserMessageBox from "./user-message-box/UserMessageBox";

const stripePromise = loadStripe(
  "pk_test_51PQ6l51kzQN5Vsox1un36EiFSRpYRchM18DD3tZCcVkTbd5IBI94J0JRWFgiWc9nlmaD3QBGH8iTiKxj4FxRv8wS00NcJaBk8R"
);

const App = () => {
  axios.defaults.withCredentials = true;
  const dispatch = useDispatch();
  const { activeContentPage, isAdmin, loggedIn } = useSelector((state: any) => state.reducer);
  const updateAndRender = (targetPage: any) => {
    dispatch(setActiveContentPage(targetPage));
    switch (targetPage) {
      case "login":
        return <LoginForm />;
      case "home":
        return <Home />;
      default:
        return <Home />;
    }
  };

  const getContentForApp = () => {
    switch (activeContentPage) {
      case "login":
        return <LoginForm />;
      case "register":
        return <RegisterForm />;
      case "services":
        return loggedIn ? <Services /> : updateAndRender("login");
      case "home":
        return <Home />;
      case "about":
        return <About />;
      case "contact":
        return <Contact />;
      case "admin":
        return isAdmin ? <Admin /> : updateAndRender("login");
      case "users":
        return isAdmin ? <AllUsersGrid /> : updateAndRender("home");
      case "userprofile":
        return loggedIn ? <UserProfile /> : updateAndRender("login");
      case "personalBanking":
        return loggedIn ? <PersonalBankingHome /> : updateAndRender("login");
      case "personalBankingCreateAccount":
        return loggedIn ? <PersonalBankingCreateAccount /> : updateAndRender("login");
      case "personalBankingAllTransactions":
        return loggedIn ? <PersonalBankingAllTransactions /> : updateAndRender("login");
      case "payment":
        return loggedIn ? <PaymentForm /> : updateAndRender("login");
      case "access":
        return isAdmin ? <AccessManager /> : updateAndRender("home");
      case "roleManager":
        return isAdmin ? <RoleManager /> : updateAndRender("home");
      case "privilegeManager":
        return isAdmin ? <PrivilegeManager /> : updateAndRender("home");
      default:
        return <Home />;
    }
  };
  return (
    <div className="app-container">
      <UserMessageBox />
      <ProgressItemsBar />
      <Navbar />
      <div className="content">
        <h1>Swiss Bank</h1>
        <Elements stripe={stripePromise}>{getContentForApp()}</Elements>
      </div>
    </div>
  );
};

export default App;
