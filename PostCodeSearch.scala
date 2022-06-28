import scala.swing._
import scala.swing.event._

class PostCodeSearch(solver: Solver) extends Frame {
  title = "住所から郵便番号を選択"
  preferredSize = new Dimension(500, 300)

  val preCombo = new ComboBox(solver.getPrefectures())
  val prefecture = new BoxPanel(Orientation.Horizontal) {
    contents += new Label("都道府県")
    contents += Swing.VStrut(10)
    contents += preCombo
  }
  val cityCombo = new ComboBox(solver.getCities(preCombo.selection.item))
  val city = new BoxPanel(Orientation.Horizontal) {
    contents += new Label("市区町村")
    contents += Swing.VStrut(10)
    contents += cityCombo
  }
  val areaCombo = new ComboBox(solver.getAreas(preCombo.selection.item, cityCombo.selection.item))
  val area = new BoxPanel(Orientation.Horizontal) {
    contents += new Label("町丁・字等")
    contents += Swing.VStrut(10)
    contents += areaCombo
  }

  contents = new BoxPanel(Orientation.Vertical) {
    contents += prefecture
    contents += city
    contents += area
  }

  listenTo(preCombo.selection)
  listenTo(cityCombo.selection)
  listenTo(areaCombo.selection)
  reactions += {
    case SelectionChanged(`preCombo`) =>
      cityCombo.peer.setModel(ComboBox.newConstantModel(solver.getCities(preCombo.selection.item)))
    case SelectionChanged(`cityCombo`) =>
      areaCombo.peer.setModel(ComboBox.newConstantModel(solver.getAreas(preCombo.selection.item, cityCombo.selection.item)))
    case SelectionChanged(`areaCombo`) =>
      solver.getPostCode(preCombo.selection.item, cityCombo.selection.item, areaCombo.selection.item) match {
        case Some(x) => Dialog.showMessage(
          title=x.prefecture + x.city + x.area,
          message="〒" + x.postcode
        )
        case _ => Dialog.showMessage(
          title="not found",
          message="該当する郵便番号は見つかりませんでした。"
        )
      }
  }

}