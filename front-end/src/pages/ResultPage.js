import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useLocation } from "react-router";
import { Redirect } from 'react-router-dom';
import { getUserResult } from '../modules/result'
import Loading from '../components/common/Loading';
import '../css/result.css'
import HeaderPage from './common/HeaderPage';
import FooterPage from './common/FooterPage';
import { Helmet } from 'react-helmet';
import queryString from "query-string"
import KakaoShareButton from '../components/share/KakaoShareButton';
import Result from '../components/inspection/Result';

const ResultPage = ({ history }) => {

  const dispatch = useDispatch();
  const { state, search, pathname } = useLocation();
  const [shareUrl, setShareUrl] = useState('');
  const inspection = useSelector(state => state.inspection);

  const calculatedResults = (answerState) => {

    let highScore = 0;
    let allAnswers = [];

    for (const key in answerState) {
      const result = key.replace('result_', '');
      const answers = answerState[key];
      let totalScore = 0;
      answers.forEach(answer => {
        totalScore += parseInt(answer.score);
      });

      if (highScore < totalScore) {
        highScore = totalScore;
      }
      allAnswers = allAnswers.concat({
        result_idx: result,
        totalScore: totalScore
      });
    }
    const results = allAnswers.filter(allAnswer => allAnswer.totalScore === highScore);
    return results.map(result => result.result_idx);
  }

  const getShareUrl = (results) => {
    let url = window.location.origin + pathname;
    const key = 'result_idx';
    url += `?${key}=${results.join(`&${key}=`)}`;
    return url;
  }

  const copyToClipboard = () => {
    const element = document.createElement('textarea');
    element.value = shareUrl;
    element.setAttribute('readonly', '');
    element.style.position = 'absolute';
    element.style.left = '-9999px';
    document.body.appendChild(element);
    element.select();
    var returnValue = document.execCommand('copy');
    document.body.removeChild(element);
    if (!returnValue) {
      throw new Error('copied nothing');
    }else{
      alert('URL이 복사되었습니다')
    }
  }

  useEffect(() => {

    const { inspection_idx } = inspection.data;
    let results = [];
    if (state && state.answerState) {
      const { answerState } = state;
      results = calculatedResults(answerState);

    } else if (search) {
      const query = queryString.parse(search);
      const { result_idx } = query;
      results = [...result_idx];
    }
    const params = {
      inspection_idx: inspection_idx,
      results: results
    }
    setShareUrl(getShareUrl(results));
    dispatch(getUserResult(params));

  }, []);

  const { data, loading, error } = useSelector(state => state.result);
  if (loading || !data) return null
  if (error) return <div>에러 발생!</div>;

  return (
    <>
      <Helmet>
        <script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
      </Helmet>
      <HeaderPage />
      {data.map((result, index) => {
        return (
          <Result result={result} key={index} />
        )
      })}
      <div className="findme__result__share">
        <div className="findme__result__share__label">
          결과 공유하기
      </div>
        <div className="findme__result__share__buttons">
          <button className="findme__result__share__buttons--kakao">
            <img src={process.env.PUBLIC_URL + "/images/icons/kakao.png"} alt='kakao_share_image' />
          </button>
          <button className="findme__result__share__buttons--link">
            <img src={process.env.PUBLIC_URL + "/images/icons/blog.png"} alt='blog_share_image' />
          </button>
          <button className="findme__result__share__buttons--link">
            <img src={process.env.PUBLIC_URL + "/images/icons/facebook.png"} alt='facebook_share_image' />
          </button>
          <button className="findme__result__share__buttons--link" onClick={copyToClipboard}>
            <img src={process.env.PUBLIC_URL + "/images/icons/url.png"} alt='url_share_image' />
          </button>
          {/* <KakaoShareButton/> */}
        </div>
      </div>

      <div className="findme__result__more">
        <div className="findme__result__more__text">
          내게 맞는 전공·직업·직무·학습법·교과목 등등<br />
          더 구체적으로 알고 싶다면
        </div>
        <button className="findme__result__more__button">
          한국진로적성센터 바로가기
        </button>
      </div>
      <FooterPage />
    </>
  )
}

export default ResultPage;