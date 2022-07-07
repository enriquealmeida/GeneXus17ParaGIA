const { createProxyMiddleware } = require('http-proxy-middleware');
const  compressionMiddleware = require('compression');

const apiProxy = createProxyMiddleware("/$data.HostRelativeUrl$", {
    target: "http://localhost",
    changeOrigin: true
  });

  const compression = compressionMiddleware();
  
module.exports = {
  port: 3030,
  open: false,
  logLevel: "silent",
  server: {
    middleware: {
      9 : compression,
      10: apiProxy,
    },
    baseDir: "dist",
    routes: {
      "/node_modules": "node_modules"
    }
  },
};