const readCookieByName = (name: string) => {
  return (
    RegExp("(^|;)\\s*" + name + "\\s*=\\s*([^;]+)")
      .exec(document.cookie)
      ?.pop() ?? ""
  );
};
export default { readCookieByName };
