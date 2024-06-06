const formatHumanReadable = (text: string) => {
  let readableText = text[0].toUpperCase();
  for (let i = 1; i < text.length; i++) {
    if (text[i].charCodeAt(0) < 91) readableText += " ";
    readableText += text[i];
  }
  return readableText;
};

export { formatHumanReadable };
