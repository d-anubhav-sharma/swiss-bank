import MenuIcon from "@mui/icons-material/Menu";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setActiveContentPage, setIsAdmin, setLoggedIn, setLoggedInUser, setNavbarExpanded } from "../store/slice";
import checkIfStaff from "../utils/CheckIfStaff";
import NavbarItemsMap from "../utils/Navbar";
import "./Navbar.css";
import checkLoginStatus from "../utils/CheckLoggedIn";
import LoginIcon from "@mui/icons-material/Login";
import LogoutIcon from "@mui/icons-material/Logout";
import AppRegistrationIcon from "@mui/icons-material/AppRegistration";
import axios from "axios";

const Navbar = () => {
  const BANKING_USER_SERVICE_BASE_URL = process.env.REACT_APP_BANKING_USER_SERVICE_BASE_URL;
  const { navbarExpanded, isAdmin, activeContentPage, loggedIn } = useSelector((state: any) => state.reducer);
  const dispatch = useDispatch();
  const getActiveNavbarItems = () => {
    let activeItems = NavbarItemsMap[activeContentPage] || NavbarItemsMap.mainNavabarItems;
    if (!loggedIn) {
      activeItems = activeItems.filter((item: any) => item.category != "user");
    }
    if (!isAdmin) {
      activeItems = activeItems.filter((item: any) => item.category != "admin");
    }
    return activeItems;
  };

  const logout = () => {
    axios.get(BANKING_USER_SERVICE_BASE_URL + "/auth/logout").then(
      () => {
        window.location.reload();
      },
      () => {}
    );
  };
  const getLoginOrRegisterItem = () => {
    if (loggedIn)
      return (
        <ul>
          <li>
            <button className={`no-styled-button ${navbarExpanded ? "" : "collapsed"}`} onClick={() => logout()}>
              <LogoutIcon />
              <span className={`nav-text ${navbarExpanded ? "" : "collapsed"}`}>Logout</span>
            </button>
          </li>
        </ul>
      );
    return (
      <ul>
        <li>
          <button
            className={`no-styled-button ${navbarExpanded ? "" : "collapsed"}`}
            onClick={() => dispatch(setActiveContentPage("login"))}
          >
            <LoginIcon />
            <span className={`nav-text ${navbarExpanded ? "" : "collapsed"}`}>Login</span>
          </button>
        </li>
        <li>
          <button
            className={`no-styled-button ${navbarExpanded ? "" : "collapsed"}`}
            onClick={() => dispatch(setActiveContentPage("register"))}
          >
            <AppRegistrationIcon />
            <span className={`nav-text ${navbarExpanded ? "" : "collapsed"}`}>Register</span>
          </button>
        </li>
      </ul>
    );
  };
  const runCheckForStaffOrAdmin = async () => {
    const isAdmin = await checkIfStaff();
    const isLoggedIn = await checkLoginStatus();
    if (isLoggedIn) {
      dispatch(setLoggedIn(true));
      dispatch(setLoggedInUser(isLoggedIn));
    } else {
      dispatch(setLoggedIn(false));
      dispatch(setLoggedInUser(""));
    }
    if (isAdmin) {
      dispatch(setIsAdmin(true));
    } else {
      dispatch(setIsAdmin(false));
    }
  };

  useEffect(() => {
    runCheckForStaffOrAdmin();
  }, []);

  return (
    <div className={`navbar ${navbarExpanded ? "" : "collapsed"}`}>
      <button className="toggle-button" onClick={() => dispatch(setNavbarExpanded(!navbarExpanded))}>
        <MenuIcon />
      </button>
      <h2 className={`navbar-title ${navbarExpanded ? "" : "collapsed"}`}>&#36;wiss Bank</h2>
      <ul>
        {getActiveNavbarItems().map((navbarItem: any) => (
          <li key={navbarItem.title} title={navbarItem.label}>
            <button
              className={`no-styled-button ${navbarExpanded ? "" : "collapsed"}`}
              onClick={() => dispatch(setActiveContentPage(navbarItem.title))}
            >
              {navbarItem.icon}
              <span className={`nav-text ${navbarExpanded ? "" : "collapsed"}`}>{navbarItem.label}</span>
            </button>
          </li>
        ))}
      </ul>
      <hr className={`ruler ${navbarExpanded ? "" : "collapsed"}`}></hr>
      {getLoginOrRegisterItem()}
      <div className="footer">
        <p className={`footer-text ${navbarExpanded ? "" : "collapsed"}`}>© 2024 Swiss Bank</p>
      </div>
    </div>
  );
};

export default Navbar;
