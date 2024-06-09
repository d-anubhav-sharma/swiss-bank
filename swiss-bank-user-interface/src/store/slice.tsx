import { createSlice } from "@reduxjs/toolkit";

const slice = createSlice({
  name: "slice",
  initialState: {
    navbarExpanded: true,
    activePage: "login",
    userMessageBoxState: {
      message: "",
      level: "",
      visible: "",
    },
    isAdmin: false,
    activeNavbarItems: [],
    activeContentPage: "about",
    loggedIn: null,
    loggedInUser: "",
    allProgressMessages: [],
    confirmationDialogBoxState: {},
    userProfileUpdatedCount: 0,
  },
  reducers: {
    setNavbarExpanded: (state, action) => {
      state.navbarExpanded = action.payload;
    },
    setActivePage: (state, action) => {
      state.activePage = action.payload;
    },
    setUserMessageBoxState: (state, action) => {
      state.userMessageBoxState = action.payload;
    },
    setIsAdmin: (state, action) => {
      state.isAdmin = action.payload;
    },
    setActiveNavbarItems: (state, action) => {
      state.activeNavbarItems = action.payload;
    },
    setActiveContentPage: (state, action) => {
      state.activeContentPage = action.payload;
    },
    setLoggedIn: (state, action) => {
      state.loggedIn = action.payload;
    },
    setLoggedInUser: (state, action) => {
      state.loggedInUser = action.payload;
    },
    setAllProgressMessages: (state, action) => {
      state.allProgressMessages = action.payload;
    },
    setConfirmationDialogBoxState: (state, action) => {
      state.confirmationDialogBoxState = action.payload;
    },
    setUserProfileUpdatedCount: (state, action) => {
      state.userProfileUpdatedCount = action.payload;
    },
  },
});

export const {
  setNavbarExpanded,
  setActivePage,
  setUserMessageBoxState,
  setIsAdmin,
  setActiveNavbarItems,
  setActiveContentPage,
  setLoggedIn,
  setLoggedInUser,
  setAllProgressMessages,
  setConfirmationDialogBoxState,
  setUserProfileUpdatedCount,
} = slice.actions;
export default slice.reducer;
