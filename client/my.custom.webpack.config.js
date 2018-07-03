const webpack = require('webpack');
// https://survivejs.com/webpack/developing/composing-configuration/
const merge = require("webpack-merge");
const path = require('path');

// https://github.com/sciabarra/sample-scajajs-react-bootstrap-with-bundler

console.log("my.custom.webpack.config");
const baseConfig = merge([require('./scalajs.webpack.config')]);
console.log("baseConfig: ", JSON.stringify(baseConfig));

const plugins = [
    new webpack.LoaderOptionsPlugin({
            debug: true
        })
];

const elementalEntry = {
    entry: {
        elementalcss: path.resolve(__dirname, "node_modules/elemental/less/elemental.less")
    }
};

const useLoaders = [
    {
        loader: 'style-loader' // creates style nodes from JS strings
    }
    ,
    {
        loader: 'css-loader', // translates CSS into CommonJS
        options: {
            exclude: [/\.js/],
           sourceMap: true
        }
    }
    ,
    {
        loader: 'less-loader', // compiles Less to CSS
        options: {
           paths: [
               path.resolve(__dirname, "node_modules/elemental/less")
           ],
           sourceMap: true
        }
    }
];

const rules = [
    {
      test: /\.less$/,
      use:useLoaders
    }
    ,
    {
      test: /\.js$/,
      use:useLoaders
    }
];

module.exports = merge([
        merge([ baseConfig/*, elementalEntry*/]),
        {
            plugins:  plugins,
            module: {
                rules: [
                    {
                        test: /\.less$/,
                        use: [
                            "style-loader",
                            "css-loader",
                            {
                                loader: "less-loader",
                                options: {
                                   paths: [
                                       path.resolve(__dirname, "node_modules/elemental/less")
                                   ],
                                   sourceMap: true
                                }
                            }
                        ]
                    }
                ]
            }
        }
    ]
);

console.log("Webpack config: \n" + JSON.stringify(module.exports, null, 4) );
