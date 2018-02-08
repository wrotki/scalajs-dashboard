var webpack = require('webpack');

module.exports = require('./scalajs.webpack.config');
//module.exports.entry["elementalcss"] = "/Users/mariusz/concurix/goworkspace/src/github.com/wrotki/scalajs-spa-playground/client/target/scala-2.11/scalajs-bundler/main/node_modules/elemental/less/elemental.less"

console.log("Entry: ", JSON.stringify(module.exports.entry))

//  "entry": {
//    "client-fastopt": "/Users/mariusz/concurix/goworkspace/src/github.com/wrotki/scalajs-spa-starter/client/target/scala-2.11/scalajs-bundler/main/fastopt-launcher.js"
//  },

// And then modify `module.exports` to extend the configuration
//module.exports.plugins = (module.exports.plugins || []).concat([
//  new UglifyJsPlugin({ sourceMap: module.exports.devtool === 'source-map' })
//]);

//module.exports.plugins = (module.exports.plugins || []).concat([
//  new webpack.LoaderOptionsPlugin({
//    debug: true
//  })])
module.exports.plugins = [
  new webpack.LoaderOptionsPlugin({
    debug: true
  })]

module.exports.module = {
        loaders: [
          { test: /\.less/, loader: 'style-loader!css-loader!less-loader' }
        ]
}

//module.exports.module = {
//        rules: [{
//            test: /\.less$/,
//            use: [{
//                loader: "style-loader" // creates style nodes from JS strings
//            }, {
//                loader: "css-loader" // translates CSS into CommonJS
//            }, {
//                loader: "less-loader" // compiles Less to CSS
//            }]
//        }]
//}
