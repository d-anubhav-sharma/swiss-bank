import LoginForm from "./login-form/LoginForm";
import Navbar from "./main-navbar/Navbar";
import RegisterForm from "./register-form/RegisterForm";

import { Elements } from "@stripe/react-stripe-js";
import { loadStripe } from "@stripe/stripe-js";
import axios from "axios";
import { useSelector } from "react-redux";
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
import AllUsersGrid from "./user-management/AllUsersGrid";
import UserProfile from "./user-management/user-profile/UserProfile";
import ProgressItemsBar from "./user-message-box/ProgressItemsBar";
import UserMessageBox from "./user-message-box/UserMessageBox";

const stripePromise = loadStripe(
  "pk_test_51PQ6l51kzQN5Vsox1un36EiFSRpYRchM18DD3tZCcVkTbd5IBI94J0JRWFgiWc9nlmaD3QBGH8iTiKxj4FxRv8wS00NcJaBk8R"
);

const App = () => {
  axios.defaults.withCredentials = true;
  const { activeContentPage, isAdmin, loggedIn } = useSelector((state: any) => state.reducer);

  const renderComponent = (component: any, condition: boolean, alternateComponent?: any) => {
    if (condition) return component;
    if (alternateComponent) return alternateComponent;
    return <Home />;
  };

  const getContentForApp = () => {
    switch (activeContentPage) {
      case "login":
        return <LoginForm />;
      case "register":
        return <RegisterForm />;
      case "services":
        return renderComponent(<Services />, loggedIn, <LoginForm />);
      case "home":
        return <Home />;
      case "about":
        return <About />;
      case "contact":
        return <Contact />;
      case "admin":
        return renderComponent(<Admin />, isAdmin, <LoginForm />);
      case "users":
        return renderComponent(<AllUsersGrid />, isAdmin, <LoginForm />);
      case "userprofile":
        return renderComponent(<UserProfile />, loggedIn, <LoginForm />);
      case "personalBanking":
        return renderComponent(<PersonalBankingHome />, loggedIn, <LoginForm />);
      case "personalBankingCreateAccount":
        return renderComponent(<PersonalBankingCreateAccount />, loggedIn, <LoginForm />);
      case "personalBankingAllTransactions":
        return renderComponent(<PersonalBankingAllTransactions />, loggedIn, <LoginForm />);
      case "payment":
        return renderComponent(<PaymentForm />, loggedIn, <LoginForm />);
      case "access":
        return renderComponent(<AccessManager />, isAdmin, <LoginForm />);
      case "roleManager":
        return renderComponent(<RoleManager />, isAdmin, <LoginForm />);
      case "privilegeManager":
        return renderComponent(<PrivilegeManager />, isAdmin, <LoginForm />);
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
        <h1>&#36;wiss Bank</h1>
        <Elements stripe={stripePromise}>{getContentForApp()}</Elements>
      </div>
    </div>
  );
};

export default App;
