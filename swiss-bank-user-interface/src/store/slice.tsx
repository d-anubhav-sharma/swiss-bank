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
    allProgressMessages: {},
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
    addProgressMessage: (state, action: any) => {
      let { id, message } = action.payload;
      state.allProgressMessages[id as keyof Object] = message;
      state.allProgressMessages = { ...state.allProgressMessages };
    },
    removeProgressMessage: (state, action) => {
      let { id } = action.payload;
      delete state.allProgressMessages[id as keyof Object];
      state.allProgressMessages = { ...state.allProgressMessages };
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
  addProgressMessage,
  removeProgressMessage,
} = slice.actions;
export default slice.reducer;
