// Production optimizations for Kotlin/JS
// Note: This file doesn't need path module - using relative paths or webpack internals

if (config.mode === 'production') {
    // Enable advanced optimizations
    config.optimization = config.optimization || {};
    
    // Modern Webpack 5 production optimizations
    config.optimization.minimize = true;
    config.optimization.sideEffects = false;
    config.optimization.usedExports = true;
    config.optimization.providedExports = true;
    config.optimization.innerGraph = true;
    
    // Configure chunk splitting for better caching
    config.optimization.splitChunks = {
        chunks: 'all',
        minSize: 20000,
        maxSize: 244000,
        cacheGroups: {
            // Separate Kotlin runtime
            kotlinRuntime: {
                test: /kotlin.*\.js$/,
                name: 'kotlin-runtime',
                chunks: 'all',
                priority: 30,
                reuseExistingChunk: true
            },
            // Separate Kobweb framework
            kobweb: {
                test: /kobweb.*\.js$/,
                name: 'kobweb-framework',
                chunks: 'all',
                priority: 20,
                reuseExistingChunk: true
            },
            // Compose runtime
            compose: {
                test: /compose.*\.js$/,
                name: 'compose-runtime',
                chunks: 'all',
                priority: 15,
                reuseExistingChunk: true
            },
            // Third-party libraries
            vendor: {
                test: /[\\/]node_modules[\\/]/,
                name: 'vendors',
                chunks: 'all',
                priority: 10,
                reuseExistingChunk: true
            }
        }
    };
    
    // Configure module IDs for better long-term caching
    config.optimization.moduleIds = 'deterministic';
    config.optimization.chunkIds = 'deterministic';
    
    // Production-specific output configuration
    config.output = config.output || {};
    config.output.filename = '[name].[contenthash:8].js';
    config.output.chunkFilename = '[name].[contenthash:8].chunk.js';
    
    // Clean output directory
    config.output.clean = true;
    
    console.log('✅ Production webpack optimizations enabled');
}