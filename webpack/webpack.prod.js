const commonConfig = require('./webpack.common.js');
const webpackMerge = require('webpack-merge');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const ExtractTextPlugin = require("extract-text-webpack-plugin");
const Visualizer = require('webpack-visualizer-plugin');
const path = require('path');
const ENV = 'prod';

module.exports = webpackMerge(commonConfig({ env: ENV }), {
    devtool: 'source-map',
    devServer: {
        contentBase: './target/www',
        proxy: [{
            context: [
                /* jhipster-needle-add-entity-to-webpack - JHipster will add entity api paths here */
                '/api',
                '/management',
                '/swagger-resources',
                '/v2/api-docs',
                '/h2-console'
            ],
            target: 'http://foodmob.vn:8080',
            secure: false,
            // changeOrigin: true,
            // pathRewrite: {
            //     '/api': '/quanlyso/api',
            //     '/management': '/quanlyso/management',
            //     '/swagger-resources': '/quanlyso/swagger-resources',
            //     '/v2/api-docs': '/quanlyso/v2/api-docs',
            //     '/h2-console': '/quanlyso/h2-console'
            // }
        }]
    },
    output: {
        path: path.resolve('./target/www'),
        filename: '[hash].[name].bundle.js',
        chunkFilename: '[hash].[id].chunk.js'
    },
    plugins: [
        new ExtractTextPlugin('[hash].styles.css'),
        new Visualizer({
            // Webpack statistics in target folder
            filename: '../stats.html'
        })
    ]
});
