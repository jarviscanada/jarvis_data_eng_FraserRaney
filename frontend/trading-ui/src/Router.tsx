import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Dashboard from './page/Dashboard/Dashboard';
import QuotePage from "./page/QuotePage/QuotePage.tsx";
import TraderAccountPage from "./page/TraderAccountPage/TraderAccountPage.tsx";

export default function Router() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Dashboard/>}/>
          <Route path="/dashboard" element={<Dashboard/>}/>
          <Route path="/quotes" element={<QuotePage/>}/>
          <Route path="/trader/:traderId" element={<TraderAccountPage/>}/>
        </Routes>
      </BrowserRouter>
  );
}
