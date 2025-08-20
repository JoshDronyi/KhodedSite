// Fix module resolution for Kotlin/JS dependencies
config.resolve = config.resolve || {};
config.resolve.modules = config.resolve.modules || [];

// Add kotlin directory to module resolution paths
config.resolve.modules.push('kotlin');

// Ensure node_modules is still included
if (!config.resolve.modules.includes('node_modules')) {
    config.resolve.modules.push('node_modules');
}

console.log('Webpack resolve modules configured:', config.resolve.modules);