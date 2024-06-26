import HomeIcon from "@mui/icons-material/Home";
import AccountBalanceIcon from "@mui/icons-material/AccountBalance";
import InfoIcon from "@mui/icons-material/Info";
import CallIcon from "@mui/icons-material/Call";
import PersonIcon from "@mui/icons-material/Person";
import HowToRegIcon from "@mui/icons-material/HowToReg";
import AdminPanelSettingsIcon from "@mui/icons-material/AdminPanelSettings";
import KeyIcon from "@mui/icons-material/Key";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import AddCardIcon from "@mui/icons-material/AddCard";
import PaidIcon from "@mui/icons-material/Paid";

const ProfileNavbarItems = [
  {
    title: "userprofile",
    label: "Profile",
    icon: <AccountCircleIcon className="icon" />,
    target: "/user-profile",
    category: "user",
  },
  {
    title: "back",
    label: "Back",
    icon: <ArrowBackIcon className="icon" />,
    target: "/back",
    category: "user",
  },
];

const PersonalBankingNavbarItems = [
  {
    title: "personalBankingCreateAccount",
    label: "Create",
    icon: <AccountCircleIcon className="icon" />,
    category: "user",
  },
  {
    title: "personalBankingAddMoneyToAccount",
    label: "Add Balance",
    icon: <AddCardIcon className="icon" />,
    category: "user",
  },
  {
    title: "personalBankingAllTransactions",
    label: "Transactions",
    icon: <PaidIcon className="icon" />,
    category: "user",
  },
  {
    title: "back",
    label: "Back",
    icon: <ArrowBackIcon className="icon" />,
    target: "/back",
    category: "admin",
  },
];

const MainNavbarItems = [
  {
    title: "userprofile",
    label: "Profile",
    icon: <AccountCircleIcon className="icon" />,
    target: "/user-profile",
    category: "user",
  },
  {
    title: "home",
    label: "Home",
    icon: <HomeIcon className="icon" />,
    target: "/home",
    category: "public",
  },
  {
    title: "services",
    label: "Services",
    icon: <AccountBalanceIcon className="icon" />,
    target: "/Services",
    category: "public",
  },
  {
    title: "about",
    label: "About",
    icon: <InfoIcon className="icon" />,
    target: "/about",
    category: "public",
  },
  {
    title: "contact",
    label: "Contact",
    icon: <CallIcon className="icon" />,
    target: "/contact",
    category: "public",
  },
  {
    title: "admin",
    label: "Admin",
    icon: <AdminPanelSettingsIcon className="icon" />,
    target: "/admin",
    category: "admin",
  },
];
const AdminNavbarItems = [
  {
    title: "users",
    label: "Users",
    icon: <PersonIcon className="icon" />,
    target: "/users",
    category: "admin",
  },
  {
    title: "approve",
    label: "Approve",
    icon: <HowToRegIcon className="icon" />,
    target: "/approve",
    category: "admin",
  },
  {
    title: "access",
    label: "Access",
    icon: <KeyIcon className="icon" />,
    target: "/access",
    category: "admin",
  },
  {
    title: "roleManager",
    label: "Roles",
    icon: <KeyIcon className="icon" />,
    target: "/roleManager",
    category: "admin",
  },
  {
    title: "privilegeManager",
    label: "Privileges",
    icon: <KeyIcon className="icon" />,
    target: "/privilegeManager",
    category: "admin",
  },
  {
    title: "back",
    label: "Back",
    icon: <ArrowBackIcon className="icon" />,
    target: "/back",
    category: "admin",
  },
];
const UserNavbarItems = [{}];
const NavbarItemsMap: any = {
  mainNavabarItems: MainNavbarItems,
  adminNavbarItems: AdminNavbarItems,
  userNavbarItems: UserNavbarItems,
  about: MainNavbarItems,
  contact: MainNavbarItems,
  admin: AdminNavbarItems,
  users: AdminNavbarItems,
  approve: AdminNavbarItems,
  access: AdminNavbarItems,
  roleManager: AdminNavbarItems,
  privilegeManager: AdminNavbarItems,
  userprofile: ProfileNavbarItems,
  personalBanking: PersonalBankingNavbarItems,
  personalBankingCreateAccount: PersonalBankingNavbarItems,
  personalBankingAllTransactions: PersonalBankingNavbarItems,
};

export default NavbarItemsMap;
