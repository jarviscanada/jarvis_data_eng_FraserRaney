export const BACKEND_URL =
    import.meta.env.VITE_BACKEND_URL ?? 'http://localhost:8080';

export const tradersUrl = `${BACKEND_URL}/dashboard/traders`;

export const createTraderUrl = `${BACKEND_URL}/trader/`;

export const deleteTraderUrl = (traderId: string) =>
    `${BACKEND_URL}/trader/traderId/${traderId}`;

export const dailyListQuotesUrl =
    `${BACKEND_URL}/quote/dailyList`;

export const traderAccountUrl = (traderId: string) =>
    `${BACKEND_URL}/dashboard/profile/${traderId}`;

export const depositFundsUrl = (traderId: string) =>
    `${BACKEND_URL}/trader/deposit/${traderId}`;

export const withdrawFundsUrl = (traderId: string) =>
    `${BACKEND_URL}/trader/withdraw/${traderId}`;
