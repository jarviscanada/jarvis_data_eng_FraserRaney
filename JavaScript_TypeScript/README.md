# JavaScript and TypeScript Exercises

This folder is dedicated to solving various JavaScript and TypeScript practice exercises. Each subfolder is a standalone exercise or example with its own tests and configuration. All examples can be installed using Node, and tests can be run with `npm test`. Testing libraries vary from example to example, including Jest, ts-jest, and Vitest.

## Exercises

- [doubly-linked-list](./doubly-linked-list)  
  TypeScript implementation (ES modules) of a doubly linked list with an array-like interface. Tests use Vitest. The following methods are implemented:
  - `push` (insert value at back)
  - `pop` (remove value at back and return)
  - `shift` (remove value at front and return)
  - `unshift` (insert value at front)

- [javascript-practice-one](./javascript-practice-one)  
  JavaScript (CommonJS) string-manipulation exercise that detects content type (HTML/CSS/TEXT) and extracts summary properties. Tests use Jest.

- [sudoku](./sudoku)  
  TypeScript (ES modules) Sudoku solver that fills the board in-place. Tests use Vitest.

- [typescript-conversion](./typescript-conversion)  
  TypeScript conversion of the `javascript-practice-one` exercise (CommonJS). Tests use ts-jest.

- [typescript-practice-one](./typescript-practice-one)  
  Collection of TypeScript exercises (15 exercises) from https://typescript-exercises.github.io/ using ES modules. Tests use a custom assertion library provided with the exercises.

## Installation

Prerequisites:
- Node.js (recommended LTS)
- npm (bundled with Node)

Install dependencies per exercise (recommended):
1. Change into the exercise folder:
    - cd JavaScript_TypeScript/<exercise-folder>
2. Install:
    - npm install

(Each subfolder is a small independent project and contains its own package.json. Installing in each folder ensures test runners and dev dependencies are available.)

## Testing

Run tests from within the exercise folder:

1. cd JavaScript_TypeScript/<exercise-folder>
2. npm test

Example (run the doubly-linked-list tests):
```bash
cd JavaScript_TypeScript/doubly-linked-list
npm install
npm test
```