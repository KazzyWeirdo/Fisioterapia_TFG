import { useRef, useState } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCalendarDays, faDumbbell, faCirclePlus, faTrash, faXmark, faCheck } from '@fortawesome/free-solid-svg-icons'
import { useSearchParams } from 'react-router-dom'
import { createTrainingSession } from '../services/trainingSessionService'
import styles from './RegisterTrainingSessionPage.module.css'

interface SetDraft {
  weightKg: string
  reps: string
  restTimeSeconds: string
  rpe: number
}

interface ExerciseDraft {
  id: number
  name: string
  sets: SetDraft[]
}

function newSet(): SetDraft {
  return { weightKg: '', reps: '', restTimeSeconds: '', rpe: 7 }
}

export default function RegisterTrainingSessionPage() {
  const [searchParams] = useSearchParams()
  const patientIdParam = searchParams.get('patientId')
  const patientName = searchParams.get('name') ?? (patientIdParam ? `Patient #${patientIdParam}` : '')

  const nextId = useRef(1)
  function newExercise(): ExerciseDraft {
    return { id: nextId.current++, name: '', sets: [newSet()] }
  }

  const today = new Date().toISOString().slice(0, 10)
  const [date, setDate] = useState(today)
  const [exercises, setExercises] = useState<ExerciseDraft[]>(() => [newExercise()])
  const [submitting, setSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState<string | null>(null)
  const [submitted, setSubmitted] = useState(false)

  function updateSet(exIdx: number, sIdx: number, field: keyof SetDraft, value: string | number) {
    setExercises(prev => prev.map((ex, i) =>
      i !== exIdx ? ex : {
        ...ex,
        sets: ex.sets.map((s, j) => j !== sIdx ? s : { ...s, [field]: value }),
      }
    ))
  }
  function addSet(exIdx: number) {
    setExercises(prev => prev.map((ex, i) =>
      i !== exIdx ? ex : { ...ex, sets: [...ex.sets, newSet()] }
    ))
  }
  function removeSet(exIdx: number, sIdx: number) {
    setExercises(prev => prev.map((ex, i) =>
      i !== exIdx ? ex : { ...ex, sets: ex.sets.filter((_, j) => j !== sIdx) }
    ))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!patientIdParam) return
    setSubmitting(true)
    setSubmitError(null)
    try {
      await createTrainingSession(Number(patientIdParam), {
        date,
        exercises: exercises.map(ex => ({
          name: ex.name,
          exercises: ex.sets.map((s, i) => ({
            setNumber: i + 1,
            weightKg: parseFloat(s.weightKg) || 0,
            reps: parseInt(s.reps) || 0,
            restTimeSeconds: parseInt(s.restTimeSeconds) || 0,
            rpe: s.rpe,
          })),
        })),
      })
      setSubmitted(true)
    } catch {
      setSubmitError('Failed to register session. Please check the fields and try again.')
    } finally {
      setSubmitting(false)
    }
  }

  if (!patientIdParam) {
    return <p className={styles.loadError}>Invalid link — no patient specified.</p>
  }

  if (submitted) {
    return (
      <div className={styles.successPage}>
        <div className={styles.successIcon}><FontAwesomeIcon icon={faCheck} /></div>
        <h2 className={styles.successTitle}>Session Registered</h2>
        <p className={styles.successMsg}>Your training session has been recorded successfully.</p>
      </div>
    )
  }

  return (
    <form className={styles.page} onSubmit={handleSubmit}>

      <div>
        <h1 className={styles.heading}>Register Training Session</h1>
        <p className={styles.subtitle}>Document patient progress with precision.</p>
      </div>

      {/* General Information */}
      <div className={styles.section}>
        <div className={styles.sectionHeader}>
          <FontAwesomeIcon icon={faCalendarDays} /> General Information
        </div>
        <div className={styles.row}>
          <div className={styles.field}>
            <label className={styles.label} htmlFor="date">SESSION DATE</label>
            <input
              id="date"
              type="date"
              className={styles.input}
              value={date}
              onChange={e => setDate(e.target.value)}
              required
            />
          </div>
          <div className={styles.field}>
            <label className={styles.label}>PATIENT REFERENCE</label>
            <div className={styles.patientDisplay}>{patientName}</div>
          </div>
        </div>
      </div>

      {/* Exercises */}
      <div className={styles.exercisesHeader}>
        <span className={styles.exercisesTitle}><FontAwesomeIcon icon={faDumbbell} /> Exercises</span>
        <button
          type="button"
          className={styles.addExerciseBtn}
          onClick={() => setExercises(prev => [...prev, newExercise()])}
        >
          <FontAwesomeIcon icon={faCirclePlus} /> Add Exercise
        </button>
      </div>

      {exercises.map((ex, exIdx) => (
        <div key={ex.id} className={styles.exerciseCard}>
          <div className={styles.exerciseCardHeader}>
            <div className={styles.exerciseCardHeaderLeft}>
              <label className={styles.exerciseNameLabel}>EXERCISE NAME</label>
              <input
                type="text"
                className={styles.exerciseNameInput}
                placeholder="Exercise name..."
                value={ex.name}
                onChange={e => setExercises(prev => prev.map((x, i) =>
                  i === exIdx ? { ...x, name: e.target.value } : x
                ))}
                required
              />
            </div>
            {exercises.length > 1 && (
              <button
                type="button"
                className={styles.deleteExerciseBtn}
                onClick={() => setExercises(prev => prev.filter((_, i) => i !== exIdx))}
              >
                <FontAwesomeIcon icon={faTrash} />
              </button>
            )}
          </div>

          <table className={styles.setsTable}>
            <thead>
              <tr>
                <th>SET</th>
                <th>WEIGHT (KG)</th>
                <th>REPS</th>
                <th>REST (SEC)</th>
                <th>RPE (1-10)</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {ex.sets.map((set, sIdx) => (
                <tr key={sIdx}>
                  <td className={styles.setNum}>{String(sIdx + 1).padStart(2, '0')}</td>
                  <td>
                    <input type="number" min="0" step="0.5" className={styles.setInput}
                      value={set.weightKg}
                      onChange={e => updateSet(exIdx, sIdx, 'weightKg', e.target.value)}
                      required />
                  </td>
                  <td>
                    <input type="number" min="1" className={styles.setInput}
                      value={set.reps}
                      onChange={e => updateSet(exIdx, sIdx, 'reps', e.target.value)}
                      required />
                  </td>
                  <td>
                    <input type="number" min="0" className={styles.setInput}
                      value={set.restTimeSeconds}
                      onChange={e => updateSet(exIdx, sIdx, 'restTimeSeconds', e.target.value)}
                      required />
                  </td>
                  <td className={styles.rpeCell}>
                    <input type="range" min="1" max="10" className={styles.rpeSlider}
                      value={set.rpe}
                      onChange={e => updateSet(exIdx, sIdx, 'rpe', Number(e.target.value))} />
                    <span className={styles.rpeValue}>{set.rpe}</span>
                  </td>
                  <td>
                    {ex.sets.length > 1 && (
                      <button type="button" className={styles.removeSetBtn}
                        onClick={() => removeSet(exIdx, sIdx)}>
                        <FontAwesomeIcon icon={faXmark} />
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <button type="button" className={styles.addSetBtn} onClick={() => addSet(exIdx)}>
            + Add Set
          </button>
        </div>
      ))}

      {submitError && <p className={styles.error}>{submitError}</p>}

      <div className={styles.footer}>
        <button
          type="button"
          className={styles.draftBtn}
          onClick={() => { setExercises([newExercise()]); setDate(today) }}
        >
          Save as Draft
        </button>
        <button type="submit" className={styles.submitBtn} disabled={submitting}>
          Complete Registration
        </button>
      </div>

    </form>
  )
}
