type Board = string[][];

export function solveSudoku(board: Board): boolean {
  const isValid = (r: number, c: number, val: string): boolean => {
    // Check row and column
    for (let i = 0; i < 9; i++) {
      if (board[r]![i] === val) return false;
      if (board[i]![c] === val) return false;
    }

    // Check 3x3 box
    const boxRow = Math.floor(r / 3) * 3;
    const boxCol = Math.floor(c / 3) * 3;
    for (let i = 0; i < 3; i++) {
      for (let j = 0; j < 3; j++) {
        if (board[boxRow + i]![boxCol + j] === val) return false;
      }
    }

    return true;
  };

  for (let row = 0; row < 9; row++) {
    for (let col = 0; col < 9; col++) {
      if (board[row]![col] === ".") {
        // Try digits 1?9
        for (let val = 1; val <= 9; val++) {
          const charVal = val.toString();
          if (isValid(row, col, charVal)) {
            board[row]![col] = charVal;

            if (solveSudoku(board)) {
              return true;
            }

            // Backtrack
            board[row]![col] = ".";
          }
        }

        // No valid digit found => trigger backtracking
        return false;
      }
    }
  }

  // Solved
  return true;
}
