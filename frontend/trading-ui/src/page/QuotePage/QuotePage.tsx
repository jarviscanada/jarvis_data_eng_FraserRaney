import QuoteList, {type Quote} from "../../component/QuoteList/QuoteList.tsx";
import NavBar from "../../component/NavBar/NavBar.tsx";
import './QuotePage.scss'
import axios from "axios";
import {dailyListQuotesUrl} from "../../utils/contants.ts";
import {useEffect, useState} from "react";
import {Button} from "antd";

function QuotePage() {

  const [quotes, setQuotes] = useState<Quote[]>([])

  const fetchDailyList = async (
      signal?: AbortSignal
  ): Promise<Quote[]> => {
    const response = await axios.get<Quote[]>(
        dailyListQuotesUrl,
        {signal}
    );

    return response.data;
  };

  const refreshQuotes = async () => {
    const quotes = await fetchDailyList();
    setQuotes(quotes);
  }

  useEffect(() => {
    const controller = new AbortController();

    const load = async () => {
      try {
        const quotes = await fetchDailyList(controller.signal);
        setQuotes(quotes);
      } catch (err) {
        if (axios.isCancel(err)) return;
        console.error(err);
      }
    };

    load();

    return () => controller.abort();
  }, []);


  return (
      <div className="quote-page">
        {/* Include NavBar below */}
        <NavBar/>
        <div className="quote-page-content">
          <div className="title">
            Quote Page
            <div className="refresh-quotes-button">
              <Button onClick={refreshQuotes}>Refresh</Button>
            </div>
          </div>
          <QuoteList quotes={quotes}/>
        </div>
      </div>
  );
}

export default QuotePage;