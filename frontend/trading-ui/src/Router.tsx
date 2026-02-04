import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Dashboard from './page/Dashboard/Dashboard';
import QuotePage from "./page/QuotePage/QuotePage.tsx";

export default function Router() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Dashboard/>}/>
          <Route path="/dashboard" element={<Dashboard/>}/>
          <Route path="/quotes" element={<QuotePage/>}/>
        </Routes>
      </BrowserRouter>
  );
}
