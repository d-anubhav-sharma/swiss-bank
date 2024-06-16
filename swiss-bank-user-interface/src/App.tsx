import LoginForm from "./login-form/LoginForm";
import Navbar from "./main-navbar/Navbar";
import RegisterForm from "./register-form/RegisterForm";

import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import About from "./about/About";
import Contact from "./contact/Contact";
import Home from "./home/Home";
import Services from "./services/Services";
import UserMessageBox from "./user-message-box/UserMessageBox";
import Admin from "./admin/Admin";
import AllUsersGrid from "./user-management/AllUsersGrid";
import { setActiveContentPage } from "./store/slice";
import UserProfile from "./user-management/user-profile/UserProfile";
import ProgressItemsBar from "./user-message-box/ProgressItemsBar";
import PersonalBankingHome from "./personal-banking/home/PersonalBankingHome";
import PersonalBankingCreateAccount from "./personal-banking/account/PersonalBankingCreateAccount";
import PaymentForm from "./personal-banking/payment/PaymentForm";
import { Elements } from "@stripe/react-stripe-js";
import { loadStripe } from "@stripe/stripe-js";
import { BrowserRouter, Route, Routes } from "react-router-dom";

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

  const getPaymentFormComponent = () => {
    return (
      <Elements stripe={stripePromise}>
        <h1>Payment Form</h1>
        <PaymentForm />
      </Elements>
    );
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
      case "payment":
        return <PaymentForm />;
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
