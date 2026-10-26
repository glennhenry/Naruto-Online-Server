window.onerror = function (message, source, lineno, colno, error) {
  const title = "JavaScript Error";
  const details = [
    message,
    source && lineno ? `File: '${source}:${lineno}'` : null,
  ]
    .filter(Boolean)
    .join("\n");

  Toast.error(title, details, false);
  return false;
};
