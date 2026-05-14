import { useEffect, useState, useMemo } from 'react'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  type ChartOptions,
} from 'chart.js'
import { Line } from 'react-chartjs-2'
import {
  getWorkloadProgression,
  getIndibaSessionStats,
  getPathologyRehabStats,
  type WorkloadPoint,
  type IndibaSessionStats,
  type PathologyRehabStats,
} from '../../services/statisticsService'
import { getTrainingSessionsFromPatient } from '../../services/trainingSessionService'
import { getIndibaSessionsFromPatient } from '../../services/indibaService'
import { useLanguage } from '../../contexts/LanguageContext'
import styles from './StatisticsTab.module.css'

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Filler, Tooltip)

interface StatisticsTabProps {
  patientId: number
}

function fmtVolume(v: number): string {
  if (v >= 1000) return `${(v / 1000).toFixed(1)}k`
  return v.toFixed(0)
}

export default function StatisticsTab({ patientId }: StatisticsTabProps) {
  const { t } = useLanguage()
  const [trainingCount, setTrainingCount] = useState(0)
  const [indibaCount, setIndibaCount] = useState(0)
  const [statsLoading, setStatsLoading] = useState(true)
  const [statsError, setStatsError] = useState<string | null>(null)
  const [indibaStats, setIndibaStats] = useState<IndibaSessionStats | null>(null)
  const [rehabStats, setRehabStats] = useState<PathologyRehabStats[]>([])
  const [exerciseInput, setExerciseInput] = useState('')
  const [exerciseName, setExerciseName] = useState('')
  const [workload, setWorkload] = useState<WorkloadPoint[]>([])
  const [workloadLoading, setWorkloadLoading] = useState(false)
  const [workloadError, setWorkloadError] = useState<string | null>(null)

  useEffect(() => {
    Promise.all([
      getTrainingSessionsFromPatient(patientId, 0, 1),
      getIndibaSessionsFromPatient(patientId, 0, 1),
      getIndibaSessionStats(patientId),
      getPathologyRehabStats(),
    ])
      .then(([training, indiba, iStats, rStats]) => {
        setTrainingCount(training.totalElements)
        setIndibaCount(indiba.totalElements)
        setIndibaStats(iStats)
        setRehabStats(rStats)
      })
      .catch(() => setStatsError(t('stats_load_error')))
      .finally(() => setStatsLoading(false))
  }, [patientId])

  useEffect(() => {
    if (!exerciseName) return
    setWorkloadLoading(true)
    setWorkloadError(null)
    getWorkloadProgression(patientId, exerciseName)
      .then(setWorkload)
      .catch(() => setWorkloadError(t('stats_workload_error')))
      .finally(() => setWorkloadLoading(false))
  }, [patientId, exerciseName])

  const currentRatio = useMemo(() => {
    const total = trainingCount + indibaCount
    return total > 0 ? trainingCount / total : null
  }, [trainingCount, indibaCount])

  const ceiling = useMemo(() => {
    if (workload.length === 0) return 1
    const maxW = Math.max(...workload.map(p => p.workload))
    return maxW > 0 ? maxW * 1.1 : 1
  }, [workload])

  const chartData = {
    labels: workload.map(p =>
      new Date(p.sessionDate + 'T00:00:00')
        .toLocaleDateString('en-US', { month: 'short', day: '2-digit' })
        .toUpperCase()
    ),
    datasets: [
      {
        data: workload.map(p => p.workload),
        borderColor: '#1a3a6b',
        backgroundColor: 'rgba(26, 58, 107, 0.06)',
        borderWidth: 2.5,
        pointBackgroundColor: 'white',
        pointBorderColor: '#1a3a6b',
        pointBorderWidth: 2,
        pointRadius: 4,
        pointHoverRadius: 6,
        fill: true,
        tension: 0,
      },
    ],
  }

  const chartOptions: ChartOptions<'line'> = {
    responsive: true,
    maintainAspectRatio: true,
    plugins: {
      legend: { display: false },
      tooltip: {
        callbacks: {
          label: ctx => `Volume: ${fmtVolume(ctx.parsed.y ?? 0)} kg`,
        },
      },
    },
    scales: {
      x: {
        grid: { color: '#e5e7eb' },
        ticks: { color: '#9ca3af', font: { size: 10 } },
      },
      y: {
        min: 0,
        suggestedMax: ceiling,
        grid: { color: '#e5e7eb' },
        ticks: {
          color: '#9ca3af',
          font: { size: 10 },
          callback: v => fmtVolume(v == null ? 0 : Number(v)),
        },
      },
    },
  }

  return (
    <div className={styles.tab}>
      <div className={styles.panels}>

        {/* Left: Transition Ratio */}
        <div className={styles.ratioCard}>
          <h3 className={styles.cardTitle}>{t('stats_transition_title')}</h3>
          <p className={styles.cardSub}>{t('stats_transition_subtitle')}</p>

          {statsLoading ? (
            <p className={styles.chartState}>{t('common_loading')}</p>
          ) : statsError ? (
            <p className={styles.chartError}>{statsError}</p>
          ) : (
            <>
              <div className={styles.gaugeWrap}>
                <div
                  className={styles.gauge}
                  style={{ '--ratio-pct': `${(currentRatio ?? 0) * 100}%` } as React.CSSProperties}
                >
                  <span className={styles.gaugeValue}>
                    {currentRatio !== null ? currentRatio.toFixed(2) : '—'}
                  </span>
                  <span className={styles.gaugeLabel}>{t('stats_ratio_label')}</span>
                </div>
              </div>

              <div className={styles.counters}>
                <div className={styles.counter}>
                  <span className={styles.counterDot} style={{ background: '#1a3a6b' }} />
                  <div>
                    <div className={styles.counterLabel}>{t('stats_training_sessions')}</div>
                    <div className={styles.counterValue}>{trainingCount}</div>
                  </div>
                </div>
                <div className={styles.counter}>
                  <span className={styles.counterDot} style={{ background: '#b45309' }} />
                  <div>
                    <div className={styles.counterLabel}>{t('stats_indiba_sessions')}</div>
                    <div className={styles.counterValue}>{indibaCount}</div>
                  </div>
                </div>
              </div>
            </>
          )}
        </div>

        {/* Right: Workload Progression */}
        <div className={styles.workloadCard}>
          <div className={styles.workloadHeader}>
            <div>
              <h3 className={styles.cardTitle}>{t('stats_workload_title')}</h3>
              <p className={styles.cardSub}>{t('stats_workload_subtitle')}</p>
            </div>
            <span className="badge bg-secondary">{t('stats_last_30_days')}</span>
          </div>

          <form
            className={styles.exerciseForm}
            onSubmit={e => {
              e.preventDefault()
              if (exerciseInput.trim()) setExerciseName(exerciseInput.trim())
            }}
          >
            <input
              type="text"
              className={styles.exerciseInput}
              placeholder={t('stats_exercise_placeholder')}
              value={exerciseInput}
              onChange={e => setExerciseInput(e.target.value)}
            />
            <button type="submit" className="btn btn-primary btn-sm">{t('stats_load_btn')}</button>
          </form>

          {workloadLoading && <p className={styles.chartState}>{t('common_loading')}</p>}
          {workloadError && <p className={styles.chartError}>{workloadError}</p>}
          {!workloadLoading && !workloadError && !exerciseName && (
            <p className={styles.chartState}>{t('stats_enter_exercise')}</p>
          )}
          {!workloadLoading && !workloadError && exerciseName && workload.length === 0 && (
            <p className={styles.chartState}>{t('stats_no_workload', { name: exerciseName })}</p>
          )}
          {!workloadLoading && !workloadError && workload.length > 0 && (
            <Line data={chartData} options={chartOptions} className={styles.chart} />
          )}
        </div>

      </div>

      {/* INDIBA Protocol Summary */}
      {!statsLoading && !statsError && indibaStats && (
        <div className={styles.ratioCard} style={{ marginTop: 24 }}>
          <h3 className={styles.cardTitle}>{t('stats_indiba_title')}</h3>
          <p className={styles.cardSub}>{t('stats_indiba_subtitle')}</p>
          <div className={styles.counters}>
            <div className={styles.counter}>
              <div>
                <div className={styles.counterLabel}>{t('stats_indiba_total')}</div>
                <div className={styles.counterValue}>{indibaStats.totalSessions}</div>
              </div>
            </div>
            <div className={styles.counter}>
              <div>
                <div className={styles.counterLabel}>{t('stats_indiba_avg_duration')}</div>
                <div className={styles.counterValue}>{indibaStats.avgDurationMinutes.toFixed(1)}</div>
              </div>
            </div>
            {indibaStats.mostTreatedArea && (
              <div className={styles.counter}>
                <div>
                  <div className={styles.counterLabel}>{t('stats_indiba_most_area')}</div>
                  <div className={styles.counterValue}>{indibaStats.mostTreatedArea}</div>
                </div>
              </div>
            )}
            {indibaStats.avgCapacitiveIntensity != null && (
              <div className={styles.counter}>
                <div>
                  <div className={styles.counterLabel}>{t('stats_indiba_avg_cap')}</div>
                  <div className={styles.counterValue}>{indibaStats.avgCapacitiveIntensity.toFixed(1)}%</div>
                </div>
              </div>
            )}
            {indibaStats.avgResistiveIntensity != null && (
              <div className={styles.counter}>
                <div>
                  <div className={styles.counterLabel}>{t('stats_indiba_avg_res')}</div>
                  <div className={styles.counterValue}>{indibaStats.avgResistiveIntensity.toFixed(1)}%</div>
                </div>
              </div>
            )}
          </div>
          {Object.keys(indibaStats.modeDistribution).length > 0 && (
            <div style={{ marginTop: 8 }}>
              <div className={styles.counterLabel}>{t('stats_indiba_modes')}</div>
              <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', marginTop: 4 }}>
                {Object.entries(indibaStats.modeDistribution).map(([mode, count]) => (
                  <span key={mode} style={{ fontSize: 13, fontWeight: 600 }}>{t('indiba_mode_' + mode.toLowerCase())}: {count}</span>
                ))}
              </div>
            </div>
          )}
        </div>
      )}

      {/* Pathology Rehab Time Table */}
      {!statsLoading && !statsError && (
        <div className={styles.workloadCard} style={{ marginTop: 24 }}>
          <div className={styles.workloadHeader}>
            <div>
              <h3 className={styles.cardTitle}>{t('stats_rehab_time_title')}</h3>
              <p className={styles.cardSub}>{t('stats_rehab_time_subtitle')}</p>
            </div>
          </div>
          {rehabStats.length === 0 ? (
            <p className={styles.chartState}>{t('stats_rehab_time_empty')}</p>
          ) : (
            <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 8, fontSize: 13 }}>
              <thead>
                <tr>
                  <th style={{ textAlign: 'left', padding: '6px 8px', borderBottom: '1px solid #e5e7eb' }}>{t('stats_rehab_time_pathology')}</th>
                  <th style={{ textAlign: 'right', padding: '6px 8px', borderBottom: '1px solid #e5e7eb' }}>{t('stats_rehab_time_avg_days')}</th>
                  <th style={{ textAlign: 'right', padding: '6px 8px', borderBottom: '1px solid #e5e7eb' }}>{t('stats_rehab_time_sample')}</th>
                </tr>
              </thead>
              <tbody>
                {rehabStats.map(row => (
                  <tr key={row.pathology}>
                    <td style={{ padding: '6px 8px', borderBottom: '1px solid #f3f4f6' }}>{t('pathology_' + row.pathology.toLowerCase())}</td>
                    <td style={{ textAlign: 'right', padding: '6px 8px', borderBottom: '1px solid #f3f4f6' }}>{row.averageDaysToDischarge.toFixed(0)}</td>
                    <td style={{ textAlign: 'right', padding: '6px 8px', borderBottom: '1px solid #f3f4f6' }}>{row.sampleSize}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}

    </div>
  )
}
