export default {
  'frontend/**/*.{ts,tsx}': (files) => {
    const relativePaths = files
      .map((f) => f.replace(/.*\/frontend\//, ''))
      .join(' ');
    return [
      `bash -c "cd frontend && npx prettier --write ${relativePaths}"`,
      `bash -c "cd frontend && npx eslint --fix ${relativePaths}"`,
    ];
  },
  'backend/**/*.java': () => ['make java-format'],
};
