const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    host: "127.0.0.1",
    port: 8080
  },
  chainWebpack: config =>{
    config.plugin('html')
        .tap(args => {
          args[0].title = "管理系统";
          return args;
        })
  }
})
