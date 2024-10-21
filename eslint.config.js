const { Linter } = require('eslint');

/** @type {import("eslint").Linter.FlatConfig[]} */
const config = [
  {
    languageOptions: {
      ecmaVersion: 2020,
      sourceType: 'module',
      globals: {
        // Define environment globals here
        // For example, for Node.js, you might want:
        process: 'readonly',
        __dirname: 'readonly'
      },
    },
    plugins: {
      '@typescript-eslint': require('@typescript-eslint/eslint-plugin')
    },
    rules: {
      'no-unused-vars': 'warn',
      indent: ['error', 2],
      quotes: ['error', 'single'],
      semi: ['error', 'always']
    },
  },
];

module.exports = config;
