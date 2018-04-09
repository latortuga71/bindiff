#ifndef CALL_GRAPH_MATCH_FUNCTION_CALL_GRAPH_MDINDEX_RELAXED_H_
#define CALL_GRAPH_MATCH_FUNCTION_CALL_GRAPH_MDINDEX_RELAXED_H_

#include "third_party/zynamics/bindiff/call_graph_match.h"

namespace security {
namespace bindiff {

// Matches functions based on their relaxed MD indices. The MD index is
// calculated without taking topological order into account. This means only the
// in-edges and out-edges in the function's local neighborhood are considered.
class MatchingStepCallGraphMdIndexRelaxed : public MatchingStep {
 public:
  MatchingStepCallGraphMdIndexRelaxed()
      : MatchingStep("function: relaxed MD index matching") {}

  bool FindFixedPoints(const FlowGraph* primary_parent,
                       const FlowGraph* secondary_parent,
                       FlowGraphs& flow_graphs_1, FlowGraphs& flow_graphs_2,
                       MatchingContext& context, MatchingSteps& matching_steps,
                       const MatchingStepsFlowGraph& default_steps) override;

 private:
  void GetUnmatchedFlowGraphsByMdindexRelaxed(
      const FlowGraphs& flow_graphs, FlowGraphDoubleMap& flow_graphs_map);
};

}  // namespace bindiff
}  // namespace security

#endif  // CALL_GRAPH_MATCH_FUNCTION_CALL_GRAPH_MDINDEX_RELAXED_H_