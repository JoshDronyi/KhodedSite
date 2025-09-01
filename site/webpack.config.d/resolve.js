// Enhanced module resolution for Kotlin/JS dependencies
const path = require('path');
global.webpackPath = path; // Share path across webpack config files

// Configure resolve before webpack processes entry points
config.resolve = config.resolve || {};
config.resolve.modules = config.resolve.modules || [];

// Add kotlin directory as primary module resolution path
config.resolve.modules.unshift(path.resolve(__dirname, 'kotlin'));
config.resolve.modules.unshift('kotlin');

// Ensure node_modules is still available
if (!config.resolve.modules.includes('node_modules')) {
    config.resolve.modules.push('node_modules');
}

// Add specific module resolution for problematic Kotlin modules
config.resolve.alias = config.resolve.alias || {};
config.resolve.alias['kotlin_org_jetbrains_kotlin_kotlin_dom_api_compat'] = 
    path.resolve(__dirname, 'kotlin/kotlin_org_jetbrains_kotlin_kotlin_dom_api_compat.js');
config.resolve.alias['kotlinx-serialization-kotlinx-serialization-core'] = 
    path.resolve(__dirname, 'kotlin/kotlinx-serialization-kotlinx-serialization-core.js');
config.resolve.alias['kotlinx-serialization-kotlinx-serialization-json'] = 
    path.resolve(__dirname, 'kotlin/kotlinx-serialization-kotlinx-serialization-json.js');

// Debug logging
console.log('Webpack resolve configuration:');
console.log('- modules:', config.resolve.modules);
console.log('- aliases:', Object.keys(config.resolve.alias || {}));