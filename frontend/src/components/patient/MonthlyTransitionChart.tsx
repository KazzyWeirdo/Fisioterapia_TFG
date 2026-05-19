import { useEffect, useMemo, useState } from 'react'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Tooltip,
  type ChartOptions,
} from 'chart.js'
import { Bar } from 'react-chartjs-2'
import {
  getTransitionRatio,
  type TransitionRatioPoint,
} from '../../services/statisticsService'
import { useLanguage } from '../../contexts/LanguageContext'
import styles from './StatisticsTab.module.css'

ChartJS.register(CategoryScale, LinearScale, BarElement, Tooltip)

interface MonthlyTransitionChartProps {
  patientId: number
}

const MONTH_LABELS = [
  'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
  'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec',
]

const ORANGE = '#f97316'
const BLUE = '#3b82f6'

function useWindowWidth(): number {
  const [width, setWidth] = useState<number>(
    typeof window !== 'undefined' ? window.innerWidth : 1024,
  )
  useEffect(() => {
    const onResize = () => setWidth(window.innerWidth)
    window.addEventListener('resize', onResize)
    return () => window.removeEventListener('resize', onResize)
  }, [])
  return width
}

export default function MonthlyTransitionChart({ patientId }: MonthlyTransitionChartProps) {
  const { t } = useLanguage()
  const currentYear = new Date().getFullYear()
  const [year, setYear] = useState<number>(currentYear)
  const [data, setData] = useState<TransitionRatioPoint[]>([])
  const [loading, setLoading] = useState<boolean>(true)
  const [error, setError] = useState<string | null>(null)

  const windowWidth = useWindowWidth()
  const isMobile = windowWidth <= 640

  useEffect(() => {
    let cancelled = false
    setLoading(true)
    setError(null)
    getTransitionRatio(patientId, year)
      .then(points => {
        if (!cancelled) setData(points)
      })
      .catch(() => {
        if (!cancelled) setError(t('stats_monthly_transition_error'))
      })
      .finally(() => {
        if (!cancelled) setLoading(false)
      })
    return () => {
      cancelled = true
    }
  }, [patientId, year])

  const sorted = useMemo(
    () => [...data]
      .filter(p => p.indibaSessions + p.trainingSessions > 0)
      .sort((a, b) => a.month - b.month),
    [data],
  )

  const chartData = useMemo(() => {
    const labels = sorted.map(p => MONTH_LABELS[p.month - 1] ?? String(p.month))
    const trainingValues = sorted.map(p => p.transitionRatio)
    const indibaValues = sorted.map(p => -(1 - p.transitionRatio))
    return {
      labels,
      datasets: [
        {
          label: t('stats_monthly_transition_training'),
          data: trainingValues,
          backgroundColor: ORANGE,
          borderColor: ORANGE,
          borderWidth: 0,
          borderRadius: 4,
          barThickness: isMobile ? 14 : 24,
        },
        {
          label: t('stats_monthly_transition_indiba'),
          data: indibaValues,
          backgroundColor: BLUE,
          borderColor: BLUE,
          borderWidth: 0,
          borderRadius: 4,
          barThickness: isMobile ? 14 : 24,
        },
      ],
    }
  }, [sorted, isMobile, t])

  const chartOptions: ChartOptions<'bar'> = useMemo(() => ({
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: {
        callbacks: {
          title: items => items[0]?.label ?? '',
          label: ctx => {
            const index = ctx.dataIndex
            const point = sorted[index]
            if (!point) return ''
            const total = point.indibaSessions + point.trainingSessions
            if (total === 0) return `Training: 0 / INDIBA: 0`
            const trainingPct = Math.round(point.transitionRatio * 100)
            const indibaPct = Math.round((1 - point.transitionRatio) * 100)
            return `Training: ${trainingPct}% / INDIBA: ${indibaPct}%`
          },
        },
      },
    },
    scales: {
      x: {
        stacked: false,
        grid: { color: '#e5e7eb' },
        ticks: {
          color: '#9ca3af',
          font: { size: 10 },
          maxRotation: isMobile ? 45 : 0,
          minRotation: isMobile ? 45 : 0,
        },
      },
      y: {
        min: -1,
        max: 1,
        grid: { color: '#e5e7eb' },
        ticks: {
          color: '#9ca3af',
          font: { size: 10 },
          stepSize: 0.25,
          callback: v => `${Math.round(Math.abs(Number(v)) * 100)}%`,
        },
      },
    },
  }), [sorted, isMobile])

  const canGoNext = year < currentYear
  const canGoPrev = year > currentYear - 1

  return (
    <div className={styles.monthlyChartCard}>
      <div className={styles.monthlyChartHeader}>
        <div>
          <h3 className={styles.cardTitle}>{t('stats_monthly_transition_title')}</h3>
          <p className={styles.cardSub}>{t('stats_monthly_transition_subtitle')}</p>
        </div>
        <div className={styles.yearSelector}>
          <button
            type="button"
            className={styles.yearBtn}
            onClick={() => setYear(y => y - 1)}
            disabled={!canGoPrev}
            aria-label={t('stats_monthly_transition_prev_year')}
          >
            ←
          </button>
          <span className={styles.yearLabel}>{year}</span>
          <button
            type="button"
            className={styles.yearBtn}
            onClick={() => setYear(y => y + 1)}
            disabled={!canGoNext}
            aria-label={t('stats_monthly_transition_next_year')}
          >
            →
          </button>
        </div>
      </div>

      {loading ? (
        <p className={styles.chartState}>{t('common_loading')}</p>
      ) : error ? (
        <p className={styles.chartError}>{error}</p>
      ) : sorted.length === 0 ? (
        <p className={styles.chartState}>{t('stats_monthly_transition_empty')}</p>
      ) : (
        <>
          <div className={styles.chartContainer}>
            <Bar data={chartData} options={chartOptions} />
          </div>
          <div className={styles.transitionLegend}>
            <div className={styles.legendItem}>
              <span className={styles.legendDot} style={{ background: ORANGE }} />
              <span className={styles.legendLabel}>{t('stats_monthly_transition_training')}</span>
            </div>
            <div className={styles.legendItem}>
              <span className={styles.legendDot} style={{ background: BLUE }} />
              <span className={styles.legendLabel}>{t('stats_monthly_transition_indiba')}</span>
            </div>
          </div>
        </>
      )}
    </div>
  )
}
