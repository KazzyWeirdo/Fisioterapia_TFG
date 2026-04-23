import { useEffect, useState, useMemo } from 'react'
import {
  getTransitionRatio,
  getWorkloadProgression,
  type TransitionRatioPoint,
  type WorkloadPoint,
} from '../../services/statisticsService'
import { getTrainingSessionsFromPatient } from '../../services/trainingSessionService'
import { getIndibaSessionsFromPatient } from '../../services/indibaService'
import styles from './StatisticsTab.module.css'

interface StatisticsTabProps {
  patientId: number
}

const CHART_W = 500
const CHART_H = 220
const PAD_L = 100
const PAD_R = 20
const PAD_T = 20
const PAD_B = 30
const plotW = CHART_W - PAD_L - PAD_R
const plotH = CHART_H - PAD_T - PAD_B

function yFor(w: number) { return PAD_T + plotH - (w / 100) * plotH }
function xFor(i: number, total: number) {
  return PAD_L + (total <= 1 ? plotW / 2 : (i / (total - 1)) * plotW)
}

const ZONES = [
  { label: 'PEAK INTENSITY', value: 80 },
  { label: 'HIGH WORKLOAD',  value: 60 },
  { label: 'BASELINE',       value: 40 },
  { label: 'RECOVERY',       value: 20 },
]

function fmtDate(raw: string): string {
  return new Date(raw + 'T00:00:00')
    .toLocaleDateString('en-US', { month: 'short', day: '2-digit' })
    .toUpperCase()
}

export default function StatisticsTab({ patientId }: StatisticsTabProps) {
  const [ratioPoints, setRatioPoints] = useState<TransitionRatioPoint[]>([])
  const [trainingCount, setTrainingCount] = useState(0)
  const [indibaCount, setIndibaCount] = useState(0)
  const [statsLoading, setStatsLoading] = useState(true)
  const [exerciseInput, setExerciseInput] = useState('')
  const [exerciseName, setExerciseName] = useState('')
  const [workload, setWorkload] = useState<WorkloadPoint[]>([])
  const [workloadLoading, setWorkloadLoading] = useState(false)
  const [workloadError, setWorkloadError] = useState<string | null>(null)

  useEffect(() => {
    const year = new Date().getFullYear()
    Promise.all([
      getTransitionRatio(patientId, year),
      getTrainingSessionsFromPatient(patientId, 0, 1),
      getIndibaSessionsFromPatient(patientId, 0, 1),
    ])
      .then(([ratio, training, indiba]) => {
        setRatioPoints(ratio)
        setTrainingCount(training.totalElements)
        setIndibaCount(indiba.totalElements)
      })
      .finally(() => setStatsLoading(false))
  }, [patientId])

  useEffect(() => {
    if (!exerciseName) return
    setWorkloadLoading(true)
    setWorkloadError(null)
    getWorkloadProgression(patientId, exerciseName)
      .then(setWorkload)
      .catch(() => setWorkloadError('Failed to load workload data'))
      .finally(() => setWorkloadLoading(false))
  }, [patientId, exerciseName])

  const currentRatio = ratioPoints.length > 0
    ? ratioPoints[ratioPoints.length - 1].transitionRatio
    : null

  const linePath = useMemo(() => {
    if (workload.length === 0) return ''
    return workload
      .map((p, i) => `${i === 0 ? 'M' : 'L'} ${xFor(i, workload.length)},${yFor(p.workload)}`)
      .join(' ')
  }, [workload])

  const labelStep = Math.max(1, Math.floor(workload.length / 5))

  return (
    <div className={styles.tab}>
      <div className={styles.panels}>

        {/* Left: Transition Ratio */}
        <div className={styles.ratioCard}>
          <h3 className={styles.cardTitle}>Patient Month Transition Ratio</h3>
          <p className={styles.cardSub}>Monthly treatment modality distribution</p>

          {statsLoading ? (
            <p className={styles.chartState}>Loading…</p>
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
                  <span className={styles.gaugeLabel}>TRANSITION RATIO</span>
                </div>
              </div>

              <div className={styles.counters}>
                <div className={styles.counter}>
                  <span className={styles.counterDot} style={{ background: '#1a3a6b' }} />
                  <div>
                    <div className={styles.counterLabel}>TRAINING SESSIONS</div>
                    <div className={styles.counterValue}>{trainingCount}</div>
                  </div>
                </div>
                <div className={styles.counter}>
                  <span className={styles.counterDot} style={{ background: '#b45309' }} />
                  <div>
                    <div className={styles.counterLabel}>INDIBA SESSIONS</div>
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
              <h3 className={styles.cardTitle}>Workload Progression</h3>
              <p className={styles.cardSub}>Evolution of intensity across current program</p>
            </div>
            <span className={styles.badge}>Last 30 Days</span>
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
              placeholder="Exercise name (e.g. Squats)"
              value={exerciseInput}
              onChange={e => setExerciseInput(e.target.value)}
            />
            <button type="submit" className={styles.exerciseBtn}>Load</button>
          </form>

          {workloadLoading && <p className={styles.chartState}>Loading…</p>}
          {workloadError && <p className={styles.chartError}>{workloadError}</p>}
          {!workloadLoading && !workloadError && !exerciseName && (
            <p className={styles.chartState}>Enter an exercise name to view progression.</p>
          )}
          {!workloadLoading && !workloadError && exerciseName && workload.length === 0 && (
            <p className={styles.chartState}>No workload data found for "{exerciseName}".</p>
          )}
          {!workloadLoading && !workloadError && workload.length > 0 && (
            <svg viewBox={`0 0 ${CHART_W} ${CHART_H}`} className={styles.chart}>
              {/* Zone lines + labels */}
              {ZONES.map(z => (
                <g key={z.label}>
                  <line
                    x1={PAD_L} y1={yFor(z.value)}
                    x2={CHART_W - PAD_R} y2={yFor(z.value)}
                    stroke="#e5e7eb" strokeWidth="1" strokeDasharray="4 3"
                  />
                  <text
                    x={PAD_L - 8} y={yFor(z.value) + 4}
                    textAnchor="end" fontSize="8" fill="#9ca3af"
                    fontFamily="sans-serif" fontWeight="600"
                  >
                    {z.label}
                  </text>
                </g>
              ))}

              {/* Shaded area under line */}
              <path
                d={`${linePath} L ${xFor(workload.length - 1, workload.length)},${PAD_T + plotH} L ${PAD_L},${PAD_T + plotH} Z`}
                fill="#1a3a6b" fillOpacity="0.06"
              />

              {/* Line */}
              <path
                d={linePath}
                fill="none" stroke="#1a3a6b" strokeWidth="2.5" strokeLinejoin="round"
              />

              {/* Dots */}
              {workload.map((p, i) => (
                <circle
                  key={i}
                  cx={xFor(i, workload.length)}
                  cy={yFor(p.workload)}
                  r={4} fill="white" stroke="#1a3a6b" strokeWidth="2"
                />
              ))}

              {/* Current intensity badge on last point */}
              {(() => {
                const last = workload[workload.length - 1]
                const x = xFor(workload.length - 1, workload.length)
                const y = yFor(last.workload)
                const label = `CURRENT INTENSITY: ${last.workload.toFixed(0)}%`
                const bw = label.length * 5.5 + 16
                return (
                  <g>
                    <rect x={x - bw + 10} y={y - 30} width={bw} height={18} rx={4} fill="#1a3a6b" />
                    <text
                      x={x - bw / 2 + 10} y={y - 18}
                      textAnchor="middle" fontSize="8" fill="white"
                      fontFamily="sans-serif" fontWeight="700"
                    >
                      {label}
                    </text>
                  </g>
                )
              })()}

              {/* X-axis date labels */}
              {workload
                .filter((_, i) => i % labelStep === 0)
                .map((p, idx) => {
                  const origIdx = idx * labelStep
                  return (
                    <text
                      key={origIdx}
                      x={xFor(origIdx, workload.length)}
                      y={CHART_H - 4}
                      textAnchor="middle" fontSize="8" fill="#9ca3af"
                      fontFamily="sans-serif"
                    >
                      {fmtDate(p.sessionDate)}
                    </text>
                  )
                })}
            </svg>
          )}
        </div>

      </div>
    </div>
  )
}
